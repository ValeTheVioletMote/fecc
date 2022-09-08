package FECharMaker

import java.awt.Color
import scala.swing.ColorChooser
import javax.swing.colorchooser.AbstractColorChooserPanel
import FECharMaker.GameColors.ColorValPalette
import scala.swing.Button
import java.awt.Dimension
import scala.swing.Dialog
import scala.swing.BoxPanel
import scala.swing.Orientation
import FECharMaker.FireEmblemCharacterCreator.ColorSelectDisplay
import FECharMaker.FireEmblemCharacterCreator.draw_images
import scala.swing.Component
import java.awt.Graphics2D

object ColorDialog {

    class ColorShadePreview() extends Component:
        val shade_width = 50
        val shade_height = 50
        preferredSize = Dimension(1, shade_height)
        override def paint(g: Graphics2D): Unit = 
            val shades = ColorDialog.shades.toSeq.sortBy( _._1 ).map(_._2)
            var x_used = 0
            for color <- shades do
                g.setColor( color )
                g.fillRect( x_used, 0, shade_width, shade_height )
                x_used = x_used + shade_width
            val new_size = Dimension( shade_width * shades.length, shade_height )
            if peer.getSize() != new_size then
                peer.setSize( new_size )
                peer.setPreferredSize( new_size )
                peer.getParent().repaint()

    enum ReqOperation:
        case Palette( disp: ColorSelectDisplay )
        case PaletteOverride( disp: ColorSelectDisplay, local_shades: Map[Int, Color] )
        case Shade( disp: ColorSelectDisplay, local_shades: Map[Int, Color], red_index: Int )
        case Basic( init: Color , cb: Color => Unit)
        case NoOp

    private var state: ReqOperation = ReqOperation.NoOp

    private val chooser = new ColorChooser {
        peer.setPreviewPanel( new ColorShadePreview().peer )
        peer.addChooserPanel( PaletteChooser )
        peer.addChooserPanel( FullPaletteChooser )
    }

    def disp: Option[ColorSelectDisplay] = 
        state match {
            case ReqOperation.Palette(disp) => Some(disp)
            case ReqOperation.Shade(disp, _, _) => Some(disp)
            case _ => None
        }

    def shades: Map[Int, Color] =
        state match {
            case ReqOperation.Shade(_, s, ridx) => s + (ridx -> chooser.color)
            case ReqOperation.Palette(d) => ColorIndices.to_shades( d.cidxs, chooser.color ).toMap
            case ReqOperation.PaletteOverride(d, s) => s 
            case ReqOperation.Basic(_, _) => Map( 0 -> chooser.color )
            case ReqOperation.NoOp => Map()
        }
    
    private val di = new Dialog() {
        val box = BoxPanel(Orientation.Vertical)
        box.contents += chooser
        val btnbox = BoxPanel(Orientation.Horizontal)
        box.contents += btnbox
        btnbox.contents ++= Seq(
            Button("Ok") {
                state match {
                    case ReqOperation.Palette(disp) => {
                        disp.set_auto_palette( chooser.color )
                    }
                    case ReqOperation.Shade(disp, local_shades, ridx) => {
                        val ns = local_shades + ( ridx -> chooser.color )
                        state = ReqOperation.Shade( disp, ns , ridx ) 
                        disp.set_shades( ns.view )
                    }
                    case ReqOperation.PaletteOverride( disp, local_shades ) => {
                        disp.set_shades( local_shades.view )
                    }
                    case ReqOperation.Basic( _, cb ) => cb(chooser.color)
                    case ReqOperation.NoOp => ()
                }
                this.close()
                draw_images()
            },
            Button("Cancel") {
                this.close()
            },
            Button("Reset") {
                state match {
                    case ReqOperation.Palette(d) => chooser.color = d.color
                    case ReqOperation.Shade(d, _, i) => { 
                        state = ReqOperation.Shade( d, d.shades.toMap, i ) 
                        chooser.color = state.asInstanceOf[ReqOperation.Shade].local_shades( i )
                    }
                    case ReqOperation.Basic(init, _) => chooser.color = init
                    case ReqOperation.PaletteOverride(d, _) => {
                        chooser.color = d.color
                        state = ReqOperation.Palette(d)
                    }
                    case ReqOperation.NoOp => ()
                }
                        
            })

        setLocationRelativeTo( FireEmblemCharacterCreator )

        contents = box
    }
    def open( color: Color, callback: Color => Unit ): Unit =
        di.visible = true
        state = ReqOperation.Basic(init=color, cb=callback)
        chooser.color = color
    def open(color_disp: ColorSelectDisplay, red_index: Option[Int] = None ): Unit =
        di.visible = true
        red_index match {
            case Some(ri) => {
                state = ReqOperation.Shade( color_disp, local_shades= color_disp.shades.toMap, red_index= ri )
                chooser.color = state.asInstanceOf[ReqOperation.Shade].local_shades( ri )
            }
            case None => {
                state = ReqOperation.Palette( color_disp )
                chooser.color = color_disp.color
            }
        }

        PaletteChooser.buildChooser()
        FullPaletteChooser.buildChooser()


    object PaletteChooser extends AbstractColorChooserPanel {

        override val preferredSize = Dimension(500, 300)
        def buildChooser(): Unit =
            PaletteChooser.setLayout( WrapLayout(0, 1, 1) )
            disp.foreach( d => {
                PaletteChooser.removeAll()
                for color <- d.prefabs do
                    add( make_color_button(color).peer )
            } )
        def updateChooser(): Unit = {}
        def getDisplayName(): String = "FE Prefabs"
        def getSmallDisplayIcon() = null
        def getLargeDisplayIcon() = null

        def make_color_button(color: Color): Button =
            val btn = Button("") {
                chooser.color = color
            }
            btn.preferredSize = Dimension(20,20)
            btn.background = color
            btn
    }

    object FullPaletteChooser extends AbstractColorChooserPanel {

        override val preferredSize = Dimension(500, 300)
        def buildChooser(): Unit =
            FullPaletteChooser.setLayout( WrapLayout(0, 1, 1) )
            disp.foreach( d => {
                FullPaletteChooser.removeAll()
                for 
                    cvp <- d.cvpalettes 
                    (civ, color) <- cvp
                    if civ == ColorIndexVal.Normal
                do
                    add( (state match {
                        case ReqOperation.Palette(_) => make_palette_button( cvp )
                        case ReqOperation.PaletteOverride(_,_) => make_palette_button( cvp )
                        case _=> PaletteChooser.make_color_button( color ) 
                    }).peer )

            } )

        def updateChooser(): Unit = {}
        def getDisplayName(): String = "FE Palettes"
        def getSmallDisplayIcon() = null
        def getLargeDisplayIcon() = null

        def make_palette_button(cvp: ColorValPalette): Button =
            val color_normal = ColorValPalette.normal_color( cvp )
            val btn = Button("") {
                state match {
                    case ReqOperation.Palette(d) => {
                        state = ReqOperation.PaletteOverride(d,  ColorValPalette.to_shades(cvp, d.cidxs) )
                    }
                    case ReqOperation.PaletteOverride(d, s) => { 
                        state = ReqOperation.PaletteOverride(d, ColorValPalette.to_shades(cvp, d.cidxs) )
                    }
                    case _ => ()
                }

                chooser.color = color_normal
            }
            btn.preferredSize = Dimension(20,20)
            btn.background = color_normal
            btn
    }



}