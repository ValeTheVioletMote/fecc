package FECharMaker

import javax.swing.border.LineBorder
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO
import scala.swing.Alignment
import scala.swing.Label
import java.awt.Dimension
import scala.swing.ScrollPane
import java.awt.image.BufferedImage
import FireEmblemCharacterCreator.Portrait
import Toolbox.ImageSelector
import javax.swing.ImageIcon

object PreviewSelector extends Elem {

    def colorize_image(img: BufferedImage, pp: PixelParser.Type, back_image: Option[BufferedImage]=None): BufferedImage =
        // val r = img.getRaster()
        val cm = Portrait.blank_img.getColorModel()
        val ret = new BufferedImage( cm , cm.createCompatibleWritableRaster(img.getWidth(), img.getHeight()) 
            , cm.isAlphaPremultiplied() , null )
        def write_img( from: BufferedImage ): Unit =
            for
                x <- 0 until from.getWidth()
                y <- 0 until from.getHeight()
                current = Color( from.getRGB(x,y), true )
                if current.getAlpha() != 0
                p = PixelParser( current, pp, DEFAULT_BORDER_COLOR ).getRGB()
            do  ret.setRGB(x, y, p)

        if(back_image.nonEmpty) write_img( back_image.get )
        write_img( img )
        ret

    var selector: ImageSelector = null

    border = LineBorder(Color.GRAY, 5)
    
    class ClickablePreview(val index: Int, src: File) extends Elem:
        CLPR =>
        val back_search = src.getName().replace(".png", "b.png")
        val img = colorize_image(ImageIO.read(src), selector.pixel_parser
        , Resources.list.find(f => f.getName() == back_search).map(ImageIO.read))
        // private val icon = ImageIcon(src.getAbsolutePath())
        private val icon = ImageIcon(img)
        private val label = Label("", icon, Alignment.Center)
        val children = Seq(ElemLiteral(label))
        listenTo(mouse.clicks)
        reactions += {
            case scala.swing.event.MouseClicked(`CLPR`, _, _, _, _) => got_click(index)
            // case t @ _ => println(t)
        }
        preferredSize = Dimension(icon.getIconWidth(), icon.getIconHeight())
        border = LineBorder(Color.BLUE)
    

    def populate(sel: ImageSelector): Unit = 
        psbody.wipe_elems()
        selector = sel
        var y_used = 0
        for crprev <- sel.files.zipWithIndex.map((f,i) => ClickablePreview(i,f)) do {
            psbody.render_elem(crprev, 0, y_used)
            y_used += crprev.preferredSize.height
        }
        // psbody.peer.setSize(Dimension(96, y_used))
        psbody.peer.setPreferredSize(Dimension(96, y_used))
        // psbody.revalidate()
        psbody.repaint()
        // scroller.repaint(Rectangle(96, y_used))
        scroller.verticalScrollBar.unitIncrement = y_used / sel.files.length / 2
        scroller.revalidate()
        // scroller.repaint()


    private val psbody = PointPanel()
    // psbody.render_elem(ElemLiteral(Label("Test me")), 0, 0)
    private val scroller = ScrollPane(psbody)
    // scroller.verticalScrollBarPolicy = ScrollPane.BarPolicy.Always

    def got_click(index: Int) = 
        selector.set_by_index( index )

    def set_size(dim: Dimension) =
        preferredSize = dim
        // psbody.preferredSize = dim
        scroller.preferredSize = dim


    val children = Seq(ElemLiteral(scroller))

}
