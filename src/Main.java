import java.util.*;

class MyHashTable<K, V> {
    private class HashNode<K, V> {
        K key;
        V value;
        HashNode<K, V> next;

        HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private HashNode<K, V>[] chainArray;
    private int M = 11;
    private int size;

    public MyHashTable() {
        chainArray = new HashNode[M];
    }

    public MyHashTable(int M) {
        this.M = M;
        chainArray = new HashNode[M];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % M;
    }

    public void put(K key, V value) {
        int index = hash(key);
        HashNode<K, V> head = chainArray[index];
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = chainArray[index];
        chainArray[index] = newNode;
        size++;
    }

    public V get(K key) {
        int index = hash(key);
        HashNode<K, V> head = chainArray[index];
        while (head != null) {
            if (head.key.equals(key)) return head.value;
            head = head.next;
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        HashNode<K, V> head = chainArray[index];
        HashNode<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev == null)
                    chainArray[index] = head.next;
                else
                    prev.next = head.next;
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }
        return null;
    }

    public boolean contains(V value) {
        for (int i = 0; i < M; i++) {
            HashNode<K, V> head = chainArray[i];
            while (head != null) {
                if (head.value.equals(value)) return true;
                head = head.next;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public int[] bucketSizes() {
        int[] sizes = new int[M];
        for (int i = 0; i < M; i++) {
            int count = 0;
            HashNode<K, V> head = chainArray[i];
            while (head != null) {
                count++;
                head = head.next;
            }
            sizes[i] = count;
        }
        return sizes;
    }
}

class BST<K extends Comparable<K>, V> implements Iterable<BST.Entry<K, V>> {
    class Node {
        K key;
        V val;
        Node left, right;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    private Node root;
    private int size;

    public void put(K key, V val) {
        if (root == null) {
            root = new Node(key, val);
            size++;
            return;
        }
        Node current = root;
        while (true) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new Node(key, val);
                    size++;
                    return;
                }
                current = current.left;
            } else if (cmp > 0) {
                if (current.right == null) {
                    current.right = new Node(key, val);
                    size++;
                    return;
                }
                current = current.right;
            } else {
                current.val = val;
                return;
            }
        }
    }

    public V get(K key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.val;
        }
        return null;
    }

    public void delete(K key) {
        Node parent = null;
        Node current = root;

        while (current != null && !current.key.equals(key)) {
            parent = current;
            if (key.compareTo(current.key) < 0)
                current = current.left;
            else
                current = current.right;
        }

        if (current == null) return;

        if (current.left == null && current.right == null) {
            if (current == root) root = null;
            else if (parent.left == current) parent.left = null;
            else parent.right = null;
        } else if (current.left == null || current.right == null) {
            Node child = (current.left != null) ? current.left : current.right;
            if (current == root) root = child;
            else if (parent.left == current) parent.left = child;
            else parent.right = child;
        } else {
            Node successorParent = current;
            Node successor = current.right;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            current.key = successor.key;
            current.val = successor.val;

            if (successorParent.left == successor)
                successorParent.left = successor.right;
            else
                successorParent.right = successor.right;
        }
        size--;
    }

    public int size() {
        return size;
    }

    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {
            Stack<Node> stack = new Stack<>();
            Node current = root;

            public boolean hasNext() {
                return current != null || !stack.isEmpty();
            }

            public Entry<K, V> next() {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }
                Node node = stack.pop();
                current = node.right;
                return new Entry<>(node.key, node.val);
            }
        };
    }
}

class MyTestingClass {
    int value;

    MyTestingClass(int value) {
        this.value = value;
    }

    public int hashCode() {
        return value * 31 + 7;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyTestingClass)) return false;
        return value == ((MyTestingClass) o).value;
    }
}

public class Main {
    public static void main(String[] args) {
        MyHashTable<MyTestingClass, Integer> table = new MyHashTable<>(11);
        Random rand = new Random();

        for (int i = 0; i < 10000; i++) {
            MyTestingClass key = new MyTestingClass(rand.nextInt(100000));
            table.put(key, i);
        }

        int[] buckets = table.bucketSizes();
        for (int i = 0; i < buckets.length; i++) {
            System.out.println("Bucket " + i + ": " + buckets[i]);
        }

        BST<Integer, String> tree = new BST<>();
        tree.put(5, "A");
        tree.put(3, "B");
        tree.put(7, "C");
        tree.put(2, "D");
        tree.put(4, "E");

        for (var elem : tree) {
            System.out.println("key is " + elem.getKey() + " and value is " + elem.getValue());
        }
    }
}