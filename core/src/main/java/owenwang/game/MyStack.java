package owenwang.game;

import owenwang.game.card.Card;

import java.util.*;

// ICS4U0
// OWEN WANG
// 752381
public class MyStack<T> extends AbstractSequentialList<T> {
    int num;

    @Override
    public ListIterator<T> listIterator(int index) {
        StackIterator it = new StackIterator();
        it.node = getNode(index);
        it.index = index;
        return it;
    }

    private class StackIterator implements ListIterator<T> {
        int index;
        Node node;
        Node previous = null;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            var value = node.value;
            previous = node;
            index++;
            node = node.next;
            return value;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            return Math.min(size(), index + 1);
        }

        @Override
        public int previousIndex() {
            return Math.max(-1, index - 1);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            previous.value = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        public Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }
        T value;
        Node next;
    }
    Node top;

    void push(T s) {
        Node old = top;
        top = new Node(s, old);
        num++;
    }

    T pop() {
        Node f = top;
        if (f != null) {
            top = f.next;
            f.next = null;
            num--;
        }
        return f == null ? null : f.value;
    }

    T peek() {
        return top.value;
    }

    @Override
    public int size() {
        return num;
    }

    private Node getNode(int index) {
        Node node = top;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    public boolean empty() {
        return num == 0;
    }

    @Override
    public void clear() {
        num = 0;
        top = null;
    }

    public void shuffle() {
        var list = new ArrayList<T>(this);
        Collections.shuffle(list);
        clear();
        for (var i : list) {
            push(i);
        }
    }
}
