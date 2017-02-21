/**
 * Created by Jiya on 2/21/17.
 */
import java.util.Date;

public class Node<T>{
    final T item;
    Date lastModified;
    Node<T> next;
    Node(T item, Date lastModified, Node<T> next){
        this.item = item;
        this.lastModified = lastModified;
        this.next = next;
    }
}
