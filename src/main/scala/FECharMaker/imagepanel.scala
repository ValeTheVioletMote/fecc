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
    border = LineBorder(Color.BLACK, 5)
    override def paintComponent(g: Graphics2D): Unit = 
        // For some reason not having this causes a bleed effect (sometimes) but unclear why
        super.paintComponent(g)
        peer.setSize( Dimension(image.getWidth(), image.getHeight) )
        g.drawImage( image, 0, 0, null )
    def setImage(new_image: BufferedImage) =
        image = new_image