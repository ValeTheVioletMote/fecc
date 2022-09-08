package FECharMaker

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import scala.swing.Panel
import java.awt.Graphics2D
import javax.swing.border.LineBorder
import java.awt.Color
import java.awt.Dimension
import java.io.File

class ImagePanel(filename: String, size: Dimension) extends Panel:
    var image: BufferedImage = ImageIO.read(new File(filename))
    border = LineBorder(Color.GRAY, ImagePanel.border_size)
    override def paintComponent(g: Graphics2D): Unit = 
        // For some reason not having this causes a bleed effect (sometimes) but unclear why
        super.paintComponent(g)
        peer.setSize( Dimension( 2*ImagePanel.border_size + size.width,  2*ImagePanel.border_size + size.height) )
        val excess_w_d2 = (size.width - image.getWidth) / 2
        val excess_h_d2 = (size.height - image.getHeight) / 2
        g.drawImage( image, ImagePanel.border_size + excess_w_d2, ImagePanel.border_size + excess_h_d2, null )
    def setImage(new_image: BufferedImage) =
        image = new_image

object ImagePanel {
    val border_size = 5
}
