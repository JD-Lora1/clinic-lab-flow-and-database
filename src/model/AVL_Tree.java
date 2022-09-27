package model;

public class AVL_Tree {
    private Node root;

    public void insert(Node node){
        if(root == null){
            root = node;
        }else{
            insert(node, root);
        }
    }
    private void insert(Node node, Node current){
        if (node.getPacient().getId() < current.getPacient().getId()) {
            //Izquierda
            if(current.getLeft() != null){
                insert(node, current.getLeft());
            }else{
                current.setLeft(node);
            }
        }
        else if(node.getPacient().getId() > current.getPacient().getId()){
            //Derecha
            if(current.getRight() != null){
                insert(node, current.getRight());
            }else{
                current.setRight(node);
            }
        } else {
            System.out.println("This user already exists");
        }
    }

    public void inorder(){
        inorder(root);
    }
    private void inorder(Node current){
        if(current == null){
            return;
        }
        inorder(current.getRight());
        System.out.println(current.getPacient().getId());
        inorder(current.getLeft());
    }

    public Node findPatient(int id){
        return findPatient(id, root);
    }

    private Node findPatient(int value, Node current){
        if(current == null){
            return null;
        }else if(current.getPacient().getId() == value){
            return current;
        } else if (value > current.getPacient().getId()) {
            return findPatient(value, current.getRight());
        } else {
            return findPatient(value, current.getLeft());
        }
    }
    public Node delete(int id){
        return delete(id, root);
    }
    private Node delete(int goal, Node current){
        if(current == null){
            return null;
        }
        if(current.getPacient().getId() == goal){
            //1. Nodo Hoja
            if(current.getLeft() == null && current.getRight() == null){
                if(current == root){
                    root = null;
                }
                return null;
            }
            //2. Nodo solo hijo izquierdo
            else if (current.getRight() == null){
                if(current == root){
                    root = current.getLeft();
                }
                return current.getLeft();
            }
            //3. Nodo solo hijo derecho
            else if(current.getLeft() == null){
                if(current == root){
                    root = current.getRight();
                }
                return current.getRight();
            }
            //4. Nodo con hijos
            else{
                Node min = findLeft(current.getRight());
                //Transferencia de valores
                current.getPacient().setId(min.getPacient().getId());
                //Hacer eliminación a partir de la derecha
                Node subarbolDER = delete(min.getPacient().getId(), current.getRight());
                current.setRight( subarbolDER );
                return current;
            }

        }else if(goal < current.getPacient().getId()){
            Node subArbolIzquierdo = delete(goal, current.getLeft());
            current.setLeft(subArbolIzquierdo);
            return current;
        }else{
            Node subArbolDerecho = delete(goal, current.getRight());
            current.setRight(subArbolDerecho);
            return current;
        }
    }
    public Node findLeft(Node current){
        if(current.getLeft() == null){
            return current;
        }
        return findLeft(current.getLeft());
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
