package drillpad.gui.extensions;

import javax.swing.JComboBox;

/**
 *
 * @author Eric Perron
 */
public class SortedComboBox extends JComboBox
{
    private final int indexToStartSort;

    /**
     * Creates a {@code SortedComboBox} that keeps all of it's items sorted in
     * alphanumerical order.
     */
    public SortedComboBox()
    {
        this(0);
    }

    /**
     * Creates a {@code SortedComboBox} that keeps it's items sorted in
     * alphanumerical order.
     * <p>
     * Items that are less than {@code indexToStartSort} will be untouched and
     * will be left in the same order as the order they were added in. This is
     * useful for a component that uses the first element in the list to trigger
     * an action such as adding an new item to the component.
     *
     * @param indexToStartSort The index from which to start sorting.
     */
    public SortedComboBox(int indexToStartSort)
    {
        this.indexToStartSort = indexToStartSort;
    }

    @Override
    public void addItem(Object e)
    {
        if (indexToStartSort > this.getItemCount())
        {
            super.addItem(e);
        }
        else
        {
            super.insertItemAt(e, getIndexToInsertInSortedOrder(e.toString()));
        }
    }

    public void removeAllSortedElements()
    {
        for (int i = getItemCount() - 1; i >= indexToStartSort; --i)
        {
            this.removeItemAt(i);
        }
    }

    /**
     * Returns the index at which to insert the new item so that all items after
     * {@code indexToStartSort} will be alphanumerically sorted.
     *
     * @param item The {@code String} item to insert.
     */
    private int getIndexToInsertInSortedOrder(String item)
    {
        int index = indexToStartSort;

        for (int i = index; i < getItemCount(); ++i)
        {
            Object currentItem = getItemAt(i);
            Object nextItem = getItemAt(i + 1);

            if (currentItem == null ||
                item.compareTo(currentItem.toString()) < 0)
            {
                break;
            }
            else if (nextItem == null)
            {
                if (item.compareTo(currentItem.toString()) > 0)
                {
                    index = i + 1;
                }
                else
                {
                    index = i;
                }
                break;
            }
            else if (item.compareTo(currentItem.toString()) > 0 &&
                     item.compareTo(nextItem.toString()) < 0)
            {
                index = i + 1;
                break;
            }
        }

        return index;
    }

}
