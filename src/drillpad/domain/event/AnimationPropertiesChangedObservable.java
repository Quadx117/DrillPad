package drillpad.domain.event;

/**
 *
 * @author Eric Perron
 */
public interface AnimationPropertiesChangedObservable
{
    /**
     * Adds an {@link AnimationPropertiesChangedObserver} to the set of
     * observers for this object, provided that it is not the same as some
     * observer already in the set. The order in which notifications will be
     * delivered to multiple observers is not specified.
     *
     * @param object an observer to be added.
     * @throws NullPointerException if the parameter {@code object} is
     *                              {@code null}.
     */
    public void addAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver object)
            throws NullPointerException;

    /**
     * Deletes an {@link AnimationPropertiesChangedObserver} from the set of
     * observers of
     * this object. Passing {@code null} to this method will have no effect.
     *
     * @param object the observer to be deleted.
     */
    public void deleteAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver object);

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public void deleteAnimationPropertiesChangedObservers();

    /**
     * Notify all of its observers.
     */
    void animationPropertiesChanged();

}
