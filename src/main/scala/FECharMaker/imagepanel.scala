package FECharMaker

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import scala.swing.Panel
import java.awt.Graphics2D
import javax.swing.border.LineBorder
import java.awt.Color
import java.awt.Dimension
import java.io.File

class ImagePanel(filename: String) extends Panel:
    var image: BufferedImage = ImageIO.read(new File(filename))
    val border_size = 5
    border = LineBorder(Color.GRAY, border_size)
    override def paintComponent(g: Graphics2D): Unit = 
        // For some reason not having this causes a bleed effect (sometimes) but unclear why
        super.paintComponent(g)
        peer.setSize( Dimension( 2*border_size + image.getWidth(),  2*border_size + image.getHeight) )
        g.drawImage( image, border_size, border_size, null )
    def setImage(new_image: BufferedImage) =
        image = new_image