package FECharMaker

/**	
 *  FireEmblemCharacterCreator created by TheFlyingMinotaur
 *  Updated by Baconmaster120
 *  Converted to Scala and updated again by Vale the Violet Mote
 *  Additional art resources provided by Iscaneus
 */

import java.awt.Color;
import java.awt.Dimension;

import javax.imageio.ImageIO;

import FECharMaker.ImagePanel;
import java.awt.image.WritableRaster;



import java.nio.file.Paths

import java.awt.Rectangle
import javax.swing.border.Border
import javax.swing.border.LineBorder
import scala.swing.FlowPanel
import scala.swing.event.EditDone
import scala.swing.event.SelectionChanged.apply
import scala.swing.event.ButtonClicked
import scala.swing.ColorChooser
import scala.swing.Window
import scala.swing.Dialog
import scala.swing.Component
import scala.swing.GridBagPanel
import scala.swing.GridPanel
import scala.swing.BorderPanel
import javax.swing.SpringLayout
import scala.swing.Graphics2D
import java.nio.file.Path
import java.io.File
import java.awt.image.BufferedImage
import scala.swing.Frame
import scala.swing.Orientation
import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.ComboBox
import scala.swing.Label
import scala.swing.Slider
import scala.swing.TextField
import scala.swing.Font
import java.awt.Transparency
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import scala.swing.ScrollPane
import javax.swing.ImageIcon
import scala.swing.Alignment
import javax.swing.colorchooser.AbstractColorChooserPanel
import java.awt.FlowLayout
import FECharMaker.WrapLayout
import scala.swing.Action
import scala.collection.MapView
import FECharMaker.GameColors.ColorValPalette
import FECharMaker.Toolbox.{ImageSelector, OptionToolbox}
import FECharMaker.ColorToolbox.{ColorSelectDisplay}
import java.io.FileInputStream
import java.io.FileWriter
import java.nio.file.Files
import java.util.regex.Pattern

object DimExtensions {
    extension (dim: Dimension) {
        def +(mod_by: Int): Dimension = Dimension( dim.width + mod_by, dim.height + mod_by )
        def +(mod_tuple: (Int, Int)): Dimension = Dimension( dim.width + mod_tuple._1, dim.height + mod_tuple._2 ) 
        def +(mod_dim: Dimension): Dimension = Dimension( dim.width + mod_dim.width, dim.height + mod_dim.height ) 


        def -(mod_by: Int): Dimension = Dimension( dim.width - mod_by, dim.height - mod_by )
        def -(mod_tuple: (Int, Int)): Dimension = Dimension( dim.width - mod_tuple._1, dim.height - mod_tuple._2 ) 
        def -(mod_dim: Dimension): Dimension = Dimension( dim.width - mod_dim.width, dim.height - mod_dim.height ) 

        def *(mod_by: Int): Dimension = Dimension( dim.width * mod_by, dim.height * mod_by )
        def *(mod_tuple: (Int, Int)): Dimension = Dimension( dim.width * mod_tuple._1, dim.height * mod_tuple._2 ) 
        def *(mod_dim: Dimension): Dimension = Dimension( dim.width * mod_dim.width, dim.height * mod_dim.height ) 

        def /(mod_by: Int): Dimension = Dimension( dim.width / mod_by, dim.height / mod_by )
        def /(mod_tuple: (Int, Int)): Dimension = Dimension( dim.width / mod_tuple._1, dim.height / mod_tuple._2 ) 
        def /(mod_dim: Dimension): Dimension = Dimension( dim.width / mod_dim.width, dim.height / mod_dim.height ) 
    }
}

import DimExtensions._


trait Elem extends Component { val children: Seq[Elem | ElemLiteral] ; var relx: Int = 0 ; var rely: Int = 0 }
case class ElemLiteral (val component: Component, var relx: Int = 0, var rely: Int = 0 )

class PointPanel extends scala.swing.Panel {
    peer.setLayout(null)
    def add(comp: scala.swing.Component, x: Int, y: Int, sx: Int, sy: Int): Unit =
        comp.preferredSize = Dimension(sx, sy)
        add(comp, x, y)
    def add(comp: scala.swing.Component, x: Int, y: Int): Unit =
        val p = comp.peer
        p.setLocation(x,y)
        p.setSize(p.getPreferredSize)
        peer.add(p)

    def wipe_elems(): Unit =
        peer.removeAll()

    def render_elem(elem: Elem | ElemLiteral, absx: Int, absy: Int): Unit =
        val (comp, nAbsx, nAbsy, children) = elem match {
            case ElemLiteral( comp, relx, rely ) => (comp, absx + relx, absy + rely, Nil) 
            case e: e with Elem => (e, absx + e.relx, absy + e.rely, e.children)
        }
        this.add(comp, nAbsx , nAbsy )
        for c <- children do render_elem(c, nAbsx, nAbsy)
}


@main def fecc = 
    FireEmblemCharacterCreator.visible = true

object Resources {
    val base = Paths.get("resources")
    val list = base.toFile().listFiles().view

}




enum ColorIndexVal:
    case Normal
    case Darker(times: Int = 1)
    case Lighter(times: Int = 1)
object ColorIndexVal {
    private def rec_dark(color: Color, times: Int): Color =
        if times == 1 then color.darker()
        else rec_dark( color.darker(), times - 1 )

    private def rec_light(color: Color, times: Int): Color =
        if times == 1 then color.brighter()
        else rec_light( color.brighter(), times - 1 )

    def to_color( base: Color, civ: ColorIndexVal ): Color =
        civ match {
            case ColorIndexVal.Normal => base
            case ColorIndexVal.Darker(times) => rec_dark(base, times)
            case ColorIndexVal.Lighter(times) => rec_light(base, times)
        }
}
type ColorIndices = Map[Int, ColorIndexVal]
object ColorIndices {
    def to_shades(cidxs: ColorIndices, color: Color): MapView[Int, Color] =
        cidxs.mapValues( ColorIndexVal.to_color(color, _) )
}




val DEFAULT_BORDER_COLOR = Color(0,0,0,255) 



object FireEmblemCharacterCreator extends Frame  {
    FireEmblemCharacterCreator.peer.setIconImage( ImageIO.read(getClass().getResource("images/tiras.png")) )

    private val GeneralPadding = 20
    private val FinalButtonsDim = Dimension( 150, 30 )
    private val FinalTextsDim = Dimension( 200 , FinalButtonsDim.height )

    trait Paneled ( var panel: ImagePanel )

    object ColorCounter extends Elem {
        private val pfx = "Unique Colors: "
        val lbl = Label(pfx)
        lbl.preferredSize = FinalTextsDim
        preferredSize = FinalTextsDim
        border = LineBorder(Color.GRAY, 1)
        val children = Seq(ElemLiteral(lbl))
        def set(num: Int) =
            lbl.text = pfx + num
    }

    object Exporter extends Elem {

        val FESmallDim = Dimension(96,96)

        private val filename_inp = TextField("base_file_name")
        filename_inp.preferredSize = FinalTextsDim

        def filename: String = filename_inp.text
        def filename_=(name: String): Unit = filename_inp.text = name 

        private val norm_export = Button("Export")(export_image(Portrait.panel.image))
        norm_export.preferredSize = FinalButtonsDim

        private val small_export = Button(s"Export ${FESmallDim.width}x${FESmallDim.height}")
            (export_image(Portrait.fesize_img, s"_${FESmallDim.width}_${FESmallDim.height}"))
        small_export.preferredSize = FinalButtonsDim

        private val token_export = Button("Export Token")(export_image(TokenImg.panel.image, "_token"))
        token_export.preferredSize = FinalButtonsDim

        private val token_small_export = Button("Token Small")
            (export_image(TokenImg.fesize_img, "_token_small"))
        token_small_export.preferredSize = FinalButtonsDim

        preferredSize = FinalTextsDim + ( 0, FinalButtonsDim.height * 4 )

        val children = Seq( ElemLiteral( filename_inp )
            , ElemLiteral(norm_export , rely = filename_inp.preferredSize.height )
            , ElemLiteral(small_export , rely = filename_inp.preferredSize.height + FinalButtonsDim.height )
            , ElemLiteral(token_export , rely = filename_inp.preferredSize.height + FinalButtonsDim.height *2 )
            , ElemLiteral(token_small_export , rely = filename_inp.preferredSize.height + FinalButtonsDim.height *3 )
        )


        def export_image(img: BufferedImage, suffix: String=""): Unit =
            val path = Paths.get(".")
            val file_out_pathstr = path.resolve(filename+suffix+".png").toAbsolutePath().toString()
            val file_out = File(file_out_pathstr)
            try
                ImageIO.write( img, "PNG", file_out )
            catch
                _ => println("Unable to write to file: " + file_out_pathstr)
    }

    class ImageViewer(private val blank: "Portrait" | "Tok", val dim: Dimension ) extends Elem:
        private val blank_path: Path = Resources.base.resolve( "Blank"+blank+".png" )
        val panel = ImagePanel( blank_path.toString(), dim )
        val blank_img = ImageIO.read( blank_path.toFile() )
        if(blank_img == null) then println( blank_path.toString()+" is bad path!" )
        var fesize_img = blank_img
        val children = Seq( ElemLiteral(panel) )
        preferredSize = dim + (ImagePanel.border_size * 2)

    // var HAIRB = Interactable()
    val Portrait = ImageViewer("Portrait", Dimension(192,192)) 
    val TokenTB = new ImageSelector( "Token", "Token", true, PixelParser.Type.Body, horizontal = true )
    val largest_token_size = TokenTB.files.map(f => ImageIO.read(f).getWidth()).reduce( (a,b) => if a > b then a else b ) * 2
    val TokenImg = ImageViewer("Tok", Dimension(largest_token_size, largest_token_size) ) 
    val Face = OptionToolbox(label_str = "Face", search_word = "Face"
                    , draw_priority = 1, pixel_parser = PixelParser.Type.Face )
    val Armor = OptionToolbox(label_str = "Armor", search_word = "Armor", draw_priority = 0 )

    val HairTB = OptionToolbox(label_str = "Hair", search_word = "Hair", draw_priority = 2 )



    val Hair = ColorSelectDisplay(label_str = "Hair", Color(224, 216, 64, 255)
        , Map( 1 -> ColorIndexVal.Lighter(), 2 -> ColorIndexVal.Normal, 3 -> ColorIndexVal.Darker() )
        , prefabs = GameColors.HairColors )

    val Eyes = ColorSelectDisplay( label_str = "Eye/Beard", color = Color(64, 50, 25, 255)
        , Map( 1 -> ColorIndexVal.Lighter(), 2 -> ColorIndexVal.Normal, 3 -> ColorIndexVal.Darker() ) 
        , prefabs = GameColors.HairColors )

    val Skin = ColorSelectDisplay( label_str = "Skin", color = Color(248, 208, 152, 255)
        , Map( 4 -> ColorIndexVal.Lighter(), 5 -> ColorIndexVal.Normal, 6 -> ColorIndexVal.Darker()
        , 7 -> ColorIndexVal.Darker(2), 8 -> ColorIndexVal.Darker(3) )
        , prefabs = GameColors.SkinColors, cvpalettes = GameColors.SkinCVP )
    val Metal = ColorSelectDisplay( label_str =  "Metal", color = Color(100, 100, 100, 255)
        , Map(  9 -> ColorIndexVal.Lighter(), 10 -> ColorIndexVal.Normal, 11 -> ColorIndexVal.Darker() )
        , prefabs = GameColors.HairColors )
    val Trim = ColorSelectDisplay( label_str = "Trim", color = Color(247, 173, 82, 255)
        , Map(  12 -> ColorIndexVal.Lighter(), 13 -> ColorIndexVal.Normal, 14 -> ColorIndexVal.Darker() )
        , prefabs = GameColors.HairColors )
    val Cloth = ColorSelectDisplay( label_str = "Cloth" , color = Color(82, 82, 115, 255) 
        , Map(  15 -> ColorIndexVal.Lighter(), 16 -> ColorIndexVal.Normal, 17 -> ColorIndexVal.Darker() )
        , prefabs = GameColors.HairColors )
    val Leather = ColorSelectDisplay( label_str = "Leather", color = Color(148, 100, 66, 255)
        , Map(  18 -> ColorIndexVal.Lighter(), 19 -> ColorIndexVal.Normal, 20 -> ColorIndexVal.Darker() )
        , prefabs = GameColors.HairColors )
    val Accessory = ColorSelectDisplay( label_str = "Accessory", color = Color(0, 0, 0, 255)
        , Map(  9 -> ColorIndexVal.Lighter(), 10 -> ColorIndexVal.Normal, 11 -> ColorIndexVal.Darker() )
        , prefabs = GameColors.HairColors )
    val AccessoryTB = OptionToolbox( label_str = "Accessory", search_word = "Acc"
                        , draw_priority = 3, pixel_parser = PixelParser.Type.Face )


    val BodyColors = Seq( Hair, Skin, Metal, Trim, Cloth, Leather )
    val FaceColors = Seq( Eyes, Skin, Accessory, Trim, Cloth, Leather )

    var ExportFileName: TextField = TextField()
    var Sliders = Array[Slider]()
    var Boxes = Array[ComboBox[String]]()


    val Body = PointPanel()
    contents = Body 

    font = Font("Calibri", Font.Bold, 12)
    title = "Fire Emblem Character Creator"


    private val __color_mapping: Seq[(String, ColorSelectDisplay)] = 
        Seq( "hair" -> Hair, "eyes" -> Eyes, "skin" -> Skin, "metal" -> Metal, "trim" -> Trim
        , "cloth" -> Cloth, "leather"->Leather, "accessory" -> Accessory )

    private val __toolbox_mapping: Seq[(String, OptionToolbox)] =
        Seq( "face" -> Face, "armor" -> Armor, "hair" -> HairTB, "accessory" -> AccessoryTB )

    val color_options = __color_mapping.map(_._2).toIndexedSeq
    val toolbox_options = __toolbox_mapping.map(_._2).toIndexedSeq

    val color_mapping = __color_mapping.toMap
    val toolbox_mapping = __toolbox_mapping.toMap


    object SaverLoader extends Elem {
        private val save_btn = Button("Save"){ export_savefile() }
        save_btn.preferredSize = FinalButtonsDim
        private val load_btn = Button("Load"){ load_into_tool() }
        load_btn.preferredSize = FinalButtonsDim

        private def sepper(f: String) = "==="+f+"==="
        private val color_start = sepper("COLOR_START")
        private val color_end = sepper("COLOR_END")
        private val toolbox_start = sepper("TOOLBOX_START")
        private val toolbox_end = sepper("TOOLBOX_END") 
        private val mapdelim = "::>"


        // Change tool values, color values, change filename
        private def load_into_tool() = {
            val path = Paths.get(".")
            val file_out_pathstr = path.resolve(Exporter.filename+".fecc")
            try
                val data = Files.readString(file_out_pathstr)
                val cs = data.indexOf( "\n", data.indexOf(color_start) )+1
                val ce = data.indexOf( "\n" + color_end )
                val ts = data.indexOf( "\n", data.indexOf(toolbox_start) )+1
                val te = data.indexOf( "\n" + toolbox_end )                

                for 
                    undelim <- data.slice(cs, ce).split("\n")
                    kv = undelim.split( Pattern.quote( mapdelim ) )
                    k = kv(0)
                    v = kv(1)
                do { 
                    // println("k:"+k)
                    // println("v:"+v)
                    color_mapping(k).load_savestring( v )
                }

                for 
                    undelim <- data.slice(ts, te).split("\n")
                    kv = undelim.split( Pattern.quote( mapdelim ) )
                    k = kv(0)
                    v = kv(1)
                do { 
                    toolbox_mapping(k).load_savestring( v ) 
                }

                val toks = data.indexOf( "\n", data.indexOf(toolbox_end) )+1
                TokenTB.set_by_index( data.slice(toks, data.length).toInt )

            catch
                _ => println("Could not load file " + file_out_pathstr.toAbsolutePath().toString() )
        }

        private def export_savefile() = {
            val colors = (
            for 
                (k,v) <- color_mapping
            yield k + mapdelim + v.savestring).mkString("\n")
            val tbxs = (
            for
                (k,v) <- toolbox_mapping
            yield k + mapdelim + v.savestring).mkString("\n")

            val data = color_start + "\n" + colors + "\n" + color_end + "\n" 
                + toolbox_start + "\n" + tbxs + "\n" + toolbox_end + "\n" 
                + TokenTB.selection_index

            val path = Paths.get(".")
            val file_out_pathstr = path.resolve(Exporter.filename+".fecc")
            try
                Files.writeString(file_out_pathstr, data)
            catch
                _ => println("Unable to write to file: " + file_out_pathstr.toAbsolutePath().toString())
            
        }
        
        preferredSize = FinalTextsDim + ( 0 , FinalButtonsDim.height )
        val children = Seq( ElemLiteral( save_btn ), ElemLiteral( load_btn, rely=FinalButtonsDim.height ) )
    }


    private var upper_right_offset = GeneralPadding
    private val row2_y = Math.max( Portrait.preferredSize.height, TokenImg.preferredSize.height)

    Body.render_elem(Portrait, upper_right_offset, 0)
    upper_right_offset += Portrait.preferredSize.width
    Body.render_elem(TokenImg, upper_right_offset, 0)
    upper_right_offset += TokenImg.preferredSize.width
    Body.render_elem(TokenTB, upper_right_offset, TokenImg.preferredSize.height - TokenTB.preferredSize.height)
    private val color_row2_xoff = TokenTB.preferredSize.width - TokenImg.preferredSize.width

    private val color_div = 4
    private var color_div_coll_x = 0
    private var color_div_largest_x = 0
    for (cd,idx) <- color_options.zipWithIndex do {
        val row = idx / color_div
        if idx % color_div == 0 then color_div_coll_x = 0
        cd.relx = color_div_coll_x
        if row == 1 then cd.relx += color_row2_xoff
        color_div_coll_x = color_div_coll_x + cd.preferredSize.width
        cd.rely = row * cd.preferredSize.height
        Body.render_elem(cd, upper_right_offset, 0)
        if color_div_coll_x > color_div_largest_x then color_div_largest_x = color_div_coll_x
    }

    upper_right_offset += (color_div_largest_x)

    for (tb, idx) <- toolbox_options.zipWithIndex do {
        tb.relx = tb.preferredSize.width * idx
        Body.render_elem(tb, GeneralPadding, row2_y)
    }

    Body.render_elem( ColorCounter, GeneralPadding + toolbox_options.length * toolbox_options(0).preferredSize.width + GeneralPadding, row2_y )

    val AntiAliasButton = Button("AntiAlias")(draw_antialias())
    AntiAliasButton.preferredSize = FinalButtonsDim
    Body.render_elem( ElemLiteral(AntiAliasButton), ColorCounter.peer.getX(), GeneralPadding + ColorCounter.peer.getY() + ColorCounter.preferredSize.height )

    Body.render_elem( Exporter, AntiAliasButton.peer.getX(), GeneralPadding + AntiAliasButton.peer.getY() + AntiAliasButton.preferredSize.height )

    Body.render_elem( SaverLoader, AntiAliasButton.peer.getX(), GeneralPadding + Exporter.peer.getY() + Exporter.preferredSize.height  )

    private val tool_height = row2_y + toolbox_options(0).preferredSize.height

    PreviewSelector.set_size(Dimension( (FinalButtonsDim.width * 1.5).toInt, tool_height ))

    Body.render_elem( PreviewSelector, GeneralPadding + upper_right_offset, 0 )
    upper_right_offset += PreviewSelector.preferredSize.width


    bounds = Rectangle(100, 100, GeneralPadding + upper_right_offset, tool_height + GeneralPadding * 3 )



        

    def draw_images(): Unit = {
        val pMin = 0;
        val portrait = deep_copy( Portrait.blank_img )
        val portrait_1t1 = deep_copy_sized( Exporter.FESmallDim.width, Exporter.FESmallDim.height )
        // var token = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
        var pixel: Color = null
        var new_pixel: Color = null

        val TBOs = toolbox_options.sortBy(_.draw_priority)

        val unique_colors = scala.collection.mutable.Set.empty[Color]


        def draw_pixel( img: BufferedImage, x: Int, y: Int, dx: Int, dy: Int, parser: PixelParser.Type, border_color: Color ): Unit = {
            val xdx = x+dx
            val ydy = y+dy
            val ox = xdx*2
            val oy = ydy*2
            if ( oy >= pMin && oy < portrait.getHeight() && ox > pMin && ox < portrait.getWidth() ) {
                pixel = Color( img.getRGB( x, y ), true )
                if(pixel.getAlpha() != 0) {
                    new_pixel = PixelParser(pixel, parser, border_color)
                    unique_colors += new_pixel
                    val rgb = new_pixel.getRGB()

                    //Image size x4

                    portrait_1t1.setRGB( xdx, ydy, rgb )
                    for 
                        dx <- 0 to 1
                        dy <- 0 to 1
                        ax = ox + dx
                        ay = oy + dy
                    do
                        portrait.setRGB( ax, ay, rgb )                        
                }
            }
        }

        for tbo <- TBOs
            b <- tbo.oselect.get_back_image()
            img = adjust_img(img=b, scale=tbo.get_scale, rotate=tbo.get_rotate)
            x <- pMin until img.getWidth()
            y <- pMin until img.getHeight()
            dy = ( ( tbo.get_offy / 100.0) * portrait.getHeight() / 2 ).toInt
            dx = ( ( tbo.get_offx / 100.0) * portrait.getWidth() / 2 ).toInt
        do draw_pixel(img=img, x=x, y=y , dx=dx, dy=dy, parser=tbo.pixel_parser, tbo.border_color_btn.get_color)

        for
            tbo <- TBOs
            img = adjust_img(img=tbo.oselect.get_image(), scale=tbo.get_scale, rotate=tbo.get_rotate)
            x <- pMin until img.getWidth()
            y <- pMin until img.getHeight()
            dy = ( ( tbo.get_offy / 100.0) * portrait.getHeight() / 2 ).toInt
            dx = ( ( tbo.get_offx / 100.0) * portrait.getWidth() / 2 ).toInt
        do draw_pixel(img=img, x=x, y=y , dx=dx, dy=dy, parser=tbo.pixel_parser, tbo.border_color_btn.get_color)

        val tok_tb_img = TokenTB.get_image()
        val token = deep_copy_sized( tok_tb_img.getWidth * 2, tok_tb_img.getHeight * 2, TokenImg.blank_img )
        val token_1t1 = deep_copy_sized( tok_tb_img.getWidth, tok_tb_img.getHeight, TokenImg.blank_img )

        for
            x <- pMin until tok_tb_img.getWidth()
            y <- pMin until tok_tb_img.getHeight()
            px = Color( tok_tb_img.getRGB( x, y ), true )
            if px.getAlpha() != 0
            np = PixelParser( px, PixelParser.Type.Body, DEFAULT_BORDER_COLOR ).getRGB()
        do {
            token_1t1.setRGB( x, y, np )
            for
                dx <- 0 to 1
                dy <- 0 to 1
                ox = x * 2
                oy = y * 2
            do {
                token.setRGB( ox + dx, oy + dy, np )
            }
        }
            


        ColorCounter.set( unique_colors.size )
        Portrait.panel.setImage( portrait )
        Portrait.fesize_img = portrait_1t1
        Portrait.panel.repaint()
        TokenImg.panel.setImage( token )
        TokenImg.fesize_img = token_1t1
        TokenImg.panel.repaint()
    }

    def draw_antialias() =
    {
        val portrait = Portrait.panel.image
        val hairimg = HairTB.oselect.get_image()
        val faceimg = Face.oselect.get_image()

        class CoordTranslator( ot: OptionToolbox ):
            val img = adjust_img(img=ot.oselect.get_image(), scale=ot.get_scale, rotate=ot.get_rotate)
            private val dx = ( ( ot.get_offx / 100.0 ) * portrait.getWidth() / 2 ).toInt
            private val dy = ( ( ot.get_offy / 100.0 ) * portrait.getHeight() / 2 ).toInt
            val img_to_portrait = (for 
                x <- 0 until img.getWidth()
                y <- 0 until img.getHeight()
                if Color( img.getRGB(x,y), true ).getAlpha() != 0
            yield ( (x,y) -> (x+dx, y+dy) )).toMap
            val portrait_to_img = img_to_portrait.map( (k,v) => (v,k) ).toMap
            def has_adjacent_img_blank( coords: (Int, Int) ): Boolean =
                has_adjacent_img_blank( coords._1, coords._2 )
            def has_adjacent_img_blank( img_x: Int, img_y: Int ): Boolean =
                var has_blank = false
                for 
                    x <- -1 to 1
                    y <- -1 to 1
                    if Color( img.getRGB( img_x+x, img_y+y ), true ).getAlpha() == 0
                do has_blank = true
            
                has_blank

            protected case class DirectionalFind( img_coords: (Int, Int), directions: (Int, Int) )
            /**
             * Given a set of coords, check all around the coords for 
                 new coords (with the direction the coords were required)
                 that exist on the other image and not on the current image
             * @return Returns a mapping of the Portrait Coords to the DirectionalFind 
                    (which has the image coords of the other image 
                    and the direction in which they were found)
            */ 
            def adjacent_points( img_coords: (Int, Int), other: CoordTranslator ) =
                val src_portrait_coords = img_to_portrait( img_coords )
                (for
                    x <- -1 to 1
                    y <- -1 to 1
                    if (x == 0 && y == 0) == false
                    && ( x != y )
                    px = src_portrait_coords._1 + x
                    py = src_portrait_coords._2 + y
                    c <- other.portrait_to_img.get( ( px, py ))
                    if portrait_to_img.contains( ( px, py ) ) == false
                yield ( (px, py) -> DirectionalFind(c, (x, y)) )).toMap
                

        val HAIRT = CoordTranslator( HairTB )
        val FACET = CoordTranslator( Face )


        val rixmap = for
            (idx, civ) <- Skin.cidxs
        yield ( ( civ match {
            case ColorIndexVal.Darker(n) => n
            case ColorIndexVal.Lighter(n) => -n
            case ColorIndexVal.Normal => 0
        } ) 
        -> (idx, Skin.shades(idx)) )
        
        val ncDark = rixmap(1)._2.getRGB()
        val ncLight = rixmap(0)._2.getRGB()

        def can_override_face_pixel( prt_x: Int, prt_y: Int, rixmap_threshold: Int = 1 ): Boolean =
            val img_coords = FACET.portrait_to_img( (prt_x, prt_y) )
            val override_ridx = Color(FACET.img.getRGB( img_coords._1, img_coords._2 ), true).getRed() / 10
            (for 
                rmi <- rixmap.find( (_, rixand) => rixand._1 == override_ridx )
            yield rmi._1 < rixmap_threshold).getOrElse(false)

        def op_on_scaled(x: Int, y: Int)(op: (new_x: Int, new_y: Int) => Unit): Unit =
            val nx = x * 2
            val ny = y * 2
            for
                ox <- 0 to 1
                oy <- 0 to 1
            do op( nx+ox, ny+oy )


        val written_pixels = scala.collection.mutable.ArrayBuffer.empty[(Int, Int)]
        for
            x <- 0 until portrait.getWidth()
            y <- 0 until portrait.getHeight()
            img_coords <- HAIRT.portrait_to_img.get( x, y )
            hair_px = Color(HAIRT.img.getRGB( img_coords._1, img_coords._2), true )
            if hair_px.getAlpha() != 0 && HAIRT.has_adjacent_img_blank( img_coords )
            aps = HAIRT.adjacent_points( img_coords, FACET )
            if aps.size > 0
        do {

            // Loop through all of the Adjacent Points - i.e. pixels next to the hair border that are part of the face
            // Set the inital color darker then move out 1 square more in the same direction as the pixel was found
                // and make that a shade lighter
            for a <- aps do {
                // Portrait coordinates
                val (apx, apy) = a._1
                
                if can_override_face_pixel(apx, apy, 2) then
                    written_pixels += ( a._1 )
                    op_on_scaled(apx, apy) { (nx, ny) =>
                        portrait.setRGB( nx, ny, ncDark )
                    }
                    Portrait.fesize_img.setRGB( apx, apy, ncDark )
                    

                // Extend the FACE img coords by another px in the same direction
                // We'll need to verify that this is again not our hair 
                    // & is a skin pixel that we can also override

                val (dx, dy) = a._2.directions
                val (ix, iy) = a._2.img_coords
                val (lix, liy) = (ix + dx, iy + dy)

                // The checks here: 
                    // First, make sure it's a color we *should* override. That is, a lighter color.
                    // We do that by getting the RedIndex & checking against the Rixmap.

                    // Then, we need to make sure the pixel is actually on the portrait
                    // Finally, check if this is a pixel we've already colored darker via written_pixels

                for
                    (epx, epy) <- FACET.img_to_portrait.get( lix, liy )
                    if written_pixels.contains( ( epx , epy ) ) == false
                    && HAIRT.portrait_to_img.contains( (epx, epy) ) == false
                    && can_override_face_pixel( epx, epy ) 
                do {
                        Portrait.fesize_img.setRGB( epx, epy, ncLight )
                        op_on_scaled( epx, epy ) { (nx, ny) =>
                            portrait.setRGB( nx, ny, ncLight )
                        }
                    }
            }

            // op_on_scaled(x, y) { (nx, ny) => 


                
            //     portrait.setRGB( nx, ny, Color(0,255,0,255).getRGB() )
            // }
            // for
        }
        Portrait.panel.repaint() 
    }

}

def deep_copy(bi: BufferedImage): BufferedImage =
    val cm = bi.getColorModel()
    new BufferedImage( cm, bi.copyData(null), cm.isAlphaPremultiplied(), null )

def deep_copy_sized(w: Int, h: Int, bi: BufferedImage = FireEmblemCharacterCreator.Portrait.blank_img): BufferedImage =
    val cm = bi.getColorModel()
    new BufferedImage( cm , cm.createCompatibleWritableRaster(w, h) 
        , cm.isAlphaPremultiplied() , null )
    



//RECOLORS EVERYTHING
    //The included images all have red values corresponding to what they are.
    //This is why eye and hair color match, because they share the same range of values (1-3)
    //E.g. face color is 51, the lighter parts are 42, and darker parts are 60+, corresponding to case 4-8.
        //Given that the max value is 25 (255), if the image eyes were changed to have red values above 210,
        //they could be separated from the hair
        //Alternatively, if the pixel could be confirmed as part of the hair or face, a simple if statement would fix it


object PixelParser {
    enum Type:
        case Body, Face
    
    private var BodyCSDMap: Map[Int, ColorSelectDisplay] = Map()
    private var BodyColorMap: Map[Int, () => Color] = Map( )
    private var FaceCSDMap: Map[Int, ColorSelectDisplay] = Map()
    private var FaceColorMap: Map[Int, () => Color] = Map( )
    private val blank = Color(0,0,0,0)
    // private val black = Color.BLACK

    private def get_csd( red_idx: Int, place: PixelParser.Type ): Option[ColorSelectDisplay] =
        if place == Type.Body && BodyCSDMap.contains(red_idx) then Some(BodyCSDMap(red_idx))
        else if place == Type.Face && FaceCSDMap.contains(red_idx) then Some(FaceCSDMap(red_idx))
        else{
            val m_csd = ( if place == Type.Body then FireEmblemCharacterCreator.BodyColors else FireEmblemCharacterCreator.FaceColors )
            .find( _.cidxs.contains( red_idx ) )
            for csd <- m_csd do
                if place == Type.Body then
                    BodyCSDMap = BodyCSDMap + ( red_idx -> csd )
                else
                    FaceCSDMap = FaceCSDMap + ( red_idx -> csd )
            m_csd
        }

    def apply(pixel: Color, place: PixelParser.Type, border_color: Color): Color =
        if pixel.getAlpha() == 0 then blank
        else
            val red_index = pixel.getRed() / 10
            if red_index == 0 then border_color
            else
                val getter = (if place == Type.Body then BodyColorMap else FaceColorMap )
                .get( red_index )
                .getOrElse( {
                    val nc = get_csd(red_index, place) match {
                        case Some(csd) => () =>  csd.shades(red_index)
                        case None => () => Color.WHITE
                    }
                    if place == Type.Body then
                        BodyColorMap = BodyColorMap + ( red_index -> nc )
                    else
                        FaceColorMap = FaceColorMap + ( red_index -> nc )
                    
                    nc
                } )


                getter()

}

def adjust_img( img: BufferedImage, scale: Float, rotate: Int ): BufferedImage =
    if scale == 1.0 && rotate == 0 then img
    else
        var ret = if scale != 1.0 then resize_buffimg( img, scale ) else img
        ret = if rotate != 0 then rotate_buffimg( ret, rotate ) else ret
        ret


def resize_buffimg(src: BufferedImage, ratio: Double): BufferedImage =
    val (h,w) = ((src.getHeight() * ratio).toInt, (src.getWidth() * ratio).toInt)
    val ret = BufferedImage(w, h, src.getTransparency())
    val at = AffineTransform()
    at.scale(ratio, ratio)
    AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(src, ret)



def rotate_buffimg(src: BufferedImage, angle: Double): BufferedImage =
    val rads = Math.toRadians( angle )
    val sin = Math.abs( Math.sin( rads ) )
    val cos = Math.abs( Math.cos( rads ) )
    val w = src.getWidth()
    val h = src.getHeight()
    val new_w: Int = Math.floor( w * cos + h * sin ).toInt
    val new_h: Int = Math.floor( h * cos + w * sin ).toInt
    val rotated = BufferedImage( new_w, new_h, if src.getTransparency() == Transparency.OPAQUE then BufferedImage.TYPE_INT_RGB else BufferedImage.TYPE_INT_ARGB )
    val g2d = rotated.createGraphics()
    val at = AffineTransform()
    at.translate( (new_w - w) / 2, (new_h - h) / 2 )
    at.rotate(rads, w/2, h/2)
    g2d.setTransform(at)
    g2d.drawImage(src, 0, 0, null)
    g2d.dispose()
    rotated
    // g2d.setColor(Color.RED)
    // g2d.drawRect(0,0,new_w-1, new_h-1)


