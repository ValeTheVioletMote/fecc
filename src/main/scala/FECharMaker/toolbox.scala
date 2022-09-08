package FECharMaker

import java.io.File
import scala.swing.ComboBox
import scala.swing.Button
import java.awt.Dimension
import javax.imageio.ImageIO
import scala.swing.Slider
import scala.swing.Label
import java.awt.Color
import scala.swing.Action
import FireEmblemCharacterCreator.draw_images

object Toolbox {
  
    private val ToolboxWidth = 200

    def elem_dim(elem: Elem | ElemLiteral): Dimension =
        elem match {
            case ElemLiteral(comp, _, _) => comp.preferredSize
            case e: e with Elem => e.preferredSize
        }

    class ImageSelector( search_word: String, label: String, alt_empty: Boolean = false, val pixel_parser: PixelParser.Type ) extends Elem:
        private val full_search = search_word + ".png"
        private var selected: String = ( if alt_empty then "Emptytok.png" else "Empty.png" )
        val files = File("resources", selected) 
            +: Resources.list.filter(_.getName().contains( full_search ) ).toList.sorted
        private val selector = ComboBox(files.map(_.getName.replaceAllLiterally("_"+full_search, "")))
        private val pieceHeight = 30
        selector.preferredSize = Dimension(ToolboxWidth,pieceHeight)
        private val lblbtn = Button(label) {  
            PreviewSelector.populate(this)
        }
        lblbtn.preferredSize = Dimension(ToolboxWidth,pieceHeight)

        // val children = Seq( ElemLiteral(new Label(label) { xLayoutAlignment = 0.5; preferredSize = Dimension(ToolboxWidth,pieceHeight) })
        // , ElemLiteral(selector, 0, pieceHeight) )

        val children = Seq( ElemLiteral(lblbtn), ElemLiteral(selector, 0, pieceHeight) )

        preferredSize = Dimension(ToolboxWidth,pieceHeight*2)

        listenTo(selector.selection)
        reactions += {
            case scala.swing.event.SelectionChanged(`selector`) => draw_images()
        }

        def set_by_index(idx: Int) = 
            selector.selection.index = idx

        def get_image() =
            ImageIO.read( files(selector.selection.index) )
        def get_back_image() =
            if( selector.selection.index == 0 ) then None
            else
                val search_for = files(selector.selection.index).getName().replace(".png", "b.png")
                Resources.list.find(f => f.getName() == search_for).map(ImageIO.read)

    class LabeledSlider( label: String, slider_min:Int= -100, slider_max:Int =100, slider_spacing:Int =50, slider_default:Int = 0 ) extends Elem:
        
        private val slider = new Slider() {
            majorTickSpacing = slider_spacing
            max = slider_max
            min = slider_min
            value = slider_default
            paintLabels = true
            paintTicks = true
            preferredSize = Dimension( ToolboxWidth, 50 )
        }
        val children = Seq( ElemLiteral(new Label(label){ preferredSize = Dimension( ToolboxWidth, 30 ) }), ElemLiteral(slider, 0, 30) )
        preferredSize = Dimension( ToolboxWidth, children.foldLeft(0: Int)(_ + elem_dim(_).height) )

        listenTo(slider)
        
        reactions += {
            case scala.swing.event.ValueChanged(`slider`) => draw_images()
        }
        
        def get_value(): Int = slider.value
        def set_value(i: Int) = slider.value = i
    


    class OffsetManipulator() extends Elem:
        val slide_offx = LabeledSlider("X Offset")
        slide_offx.set_value(0)
        val slide_offy = LabeledSlider("Y Offset")
        slide_offy.set_value(0)
        slide_offy.rely = slide_offx.preferredSize.height

        val slide_scale = LabeledSlider("Scale", slider_min=0, slider_max = 300, slider_spacing=50, slider_default = 100)
        slide_scale.rely = slide_offx.preferredSize.height*2

        val slide_rotate = LabeledSlider("Rotate", slider_min= -180, slider_max = 180, slider_spacing=60)
        slide_rotate.rely = slide_offx.preferredSize.height*3

        def get_offx = slide_offx.get_value()
        def get_offy = slide_offy.get_value()
        def get_scale = slide_scale.get_value().toFloat / 100.0f
        def get_rotate = slide_rotate.get_value()
        val children = Seq( slide_offx, slide_offy, slide_scale, slide_rotate )

        preferredSize = Dimension(ToolboxWidth, slide_offx.preferredSize.height * 4)


    class BorderColorButton(color: Color) extends Button with Elem {
        private var _color = color
        background = _color
        def set_color( new_color: Color ) =
            _color = new_color
            background = _color
        action = Action(""){
            ColorDialog.open(_color, set_color)
        }
        def get_color: Color = _color
        val children = Nil
    }

    class OptionToolbox(label_str: String, search_word: String, val draw_priority: Int
    , val pixel_parser: PixelParser.Type = PixelParser.Type.Body, alt_empty: Boolean = false) 
    extends Elem:
        val oselect = ImageSelector(search_word, label_str, alt_empty, pixel_parser)
        val omanip = OffsetManipulator()
        omanip.rely = oselect.preferredSize.height
        val border_color_btn = BorderColorButton( DEFAULT_BORDER_COLOR )
        border_color_btn.preferredSize = Dimension( ToolboxWidth, 30)
        border_color_btn.rely = omanip.rely + omanip.preferredSize.height
        val children = Seq(oselect, omanip, border_color_btn)
        preferredSize = Dimension( ToolboxWidth, oselect.preferredSize.height + omanip.preferredSize.height + border_color_btn.preferredSize.height )
        def get_offx = omanip.get_offx
        def get_offy = omanip.get_offy
        def get_scale = omanip.get_scale
        def get_rotate = omanip.get_rotate
}
