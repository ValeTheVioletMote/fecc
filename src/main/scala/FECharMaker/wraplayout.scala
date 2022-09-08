package FECharMaker

import java.awt._
import javax.swing.JScrollPane
import javax.swing.SwingUtilities

/**
 *  FlowLayout subclass that fully supports wrapping of components.
 */
class WrapLayout(align: Integer = 0, hgap: Integer = 5, vgap: Integer = 5) extends FlowLayout(align, hgap, vgap) {
    // private var preferredLayoutSize: Dimension
    

	// /**
	// * Constructs a new <code>WrapLayout</code> with a left
	// * alignment and a default 5-unit horizontal and vertical gap.
	// */
    // def apply() = super()
    // /**
	// * Constructs a new <code>FlowLayout</code> with the specified
	// * alignment and a default 5-unit horizontal and vertical gap.
	// * The value of the alignment argument must be one of
	// * <code>WrapLayout</code>, <code>WrapLayout</code>,
	// * or <code>WrapLayout</code>.
	// * @param align the alignment value
	// */
    // def apply(align: Int) = super(align)
    // /**
	// * Creates a new flow layout manager with the indicated alignment
	// * and the indicated horizontal and vertical gaps.
	// * <p>
	// * The value of the alignment argument must be one of
	// * <code>WrapLayout</code>, <code>WrapLayout</code>,
	// * or <code>WrapLayout</code>.
	// * @param align the alignment value
	// * @param hgap the horizontal gap between components
	// * @param vgap the vertical gap between components
	// */
    
    // def apply(align: Int, hgap: Int, vgap: Int) = super(align, hgap, vgap)

    /**
	* Returns the preferred dimensions for this layout given the
	* <i>visible</i> components in the specified target container.
	* @param target the component which needs to be laid out
	* @return the preferred dimensions to lay out the
	* subcomponents of the specified container
	*/
    override def preferredLayoutSize(target: Container): Dimension =
        layoutSize(target, true)



    /**
	* Returns the minimum or preferred dimension needed to layout the target
	* container.
	*
	* @param target target to get layout size for
	* @param preferred should preferred size be calculated
	* @return the dimension to layout the target container
	*/
    def layoutSize(target: Container, preferred: Boolean): Dimension =
    {
        synchronized( target.getTreeLock() )
        {
            //  Each row must fit with the width allocated to the containter.
            //  When the container width = 0, the preferred width of the container
            //  has not yet been calculated so lets ask for the maximum.
            var container = target
            while( container.getSize().width == 0 && container.getParent() != null )
            do container = container.getParent()
            
            val target_width = container.getSize().width match {
                case 0 => Integer.MAX_VALUE
                case otherwise => otherwise
            }
            val hgap = getHgap()
            val vgap = getVgap()
            val insets = target.getInsets()
            val horizontal_insets_and_gap = insets.left + insets.right + (hgap * 2)
            var max_width = target_width - horizontal_insets_and_gap

            val dim = Dimension(0,0)
            var row_width = 0
            var row_height = 0
            for 
                m <- target.getComponents() 
                if m.isVisible()
                d = if preferred then m.getPreferredSize() else m.getMinimumSize()
            do
                if row_width + d.width > max_width  then 
                    row_added(dim, row_width, row_height)
                    row_width = 0; max_width = 0
                
                if row_width != 0  then row_width = row_width + hgap 

                row_width = row_width + d.width
                row_height = Math.max( row_height, d.height )
            
            row_added(dim, row_width, row_height)
            dim.width = dim.width + horizontal_insets_and_gap
            dim.height = dim.height + insets.top + insets.bottom + vgap * 2
            for 
                scrollPane <- Option(SwingUtilities.getAncestorOfClass(classOf[JScrollPane], target))
                if target.isValid()
            do dim.width = dim.width - ( hgap + 1 )

            dim
        }
    }

    def row_added(dim: Dimension, row_width: Int, row_height: Int): Unit =
        dim.width = Math.max(dim.width, row_width)
        if dim.height > 0 then dim.height = dim.height + getVgap()
        dim.height = dim.height + row_height

}

