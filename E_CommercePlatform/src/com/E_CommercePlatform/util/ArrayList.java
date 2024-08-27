
package com.E_CommercePlatform.util;

import java.util.Arrays;

public class ArrayList<T> {
    private Object[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public ArrayList() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    public void add(T element) {
        addFirst(element);
    }

    public void addFirst(T element) {
        ensureCapacity();
        for (int i = size; i > 0; i--) {
            elements[i] = elements[i - 1];
        }
        elements[0] = element;
        size++;
    }

    public void addLast(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false; // Index is out of bounds
        }

        // Shift elements to the left from the index
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null; // Clear the last element
        return true;
    }

    public boolean removeFirst() {
        if (size == 0)
            return false;
        for (int i = 0; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return true;
    }

    public boolean removeLast() {
        if (size == 0)
            return false;
        elements[--size] = null;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }
        return -1; // Return -1 if the element is not found
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.print(elements[i] + " ");
        }
        System.out.println();
    }

    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T oldElement = (T) elements[index];
        elements[index] = element;
        return oldElement;
    }

    public void clear() {
        // Nullify all elements to help with garbage collection
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

}
