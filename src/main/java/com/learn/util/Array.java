package com.learn.util;

public class Array<E> {

    private E[] data;
    private int size;

    //构建函数，传入数组的容量capacity构造Array
    public Array(int capacity){
        data=(E[])new Object[capacity];
        size=0;
    }

    //无参数的构建函数，默认数组的容量capacity=10
    public Array(){
        this(100);
    }

    //获取数组的容量
    public int getCapacity(){
        return data.length;
    }

    //获取数组的元素个数
    public int getSize(){
        return size;
    }

    //返回数组是否为空
    public boolean isEmpty(){
        return size==0;
    }

    //在index索引的位置插入一个新元素e
    public void add(int index,E e){
        if(size==data.length){
            throw new IllegalArgumentException("Add failed. Array is full.");
        }
        if(index<0||index>size){
            throw new IllegalArgumentException("Add failed. Required index>=0 and index<=size.");
        }
        for(int i=size-1;i>=index;i--){
            data[i+1]=data[i];
        }
        data[index]=e;
        size++;
    }

    //向最后一个元素后添加一个新元素
    public void addLast(E e){
        add(size,e);
    }

    //在第一个元素前添加一个新元素
    public void addFirst(E e){
        add(0,e);
    }

    //获取索引位置的元素e
    public E get(int index){
        if(index<0||index>=size){
            throw new IllegalArgumentException("Get failed. Index is illegal.");
        }
        return data[index];
    }

    public void set(int index,E e){
        if(index<0||index>=size){
            throw new IllegalArgumentException("Set failed. Index is illegal.");
        }
        data[index]=e;
    }



    @Override
    public String toString() {
        StringBuilder res=new StringBuilder();
        res.append(String.format("Array: size = %d , capacity = %d\n",size,data.length));
        res.append("[");
        for(int i=0;i<size;i++){
            res.append(data[i]);
            if (i!=size-1){
                res.append(", ");
            }
        }
        res.append("]");
        return res.toString();
    }

    //查找数组中是否有元素e
    public boolean contains(E e){
        for(int i =0; i<size;i++){
            if (data[i]==e){
                return true;
            }
        }
        return false;
    }

    //查找数组中元素e所在的索引，如果不存在，则返回-1
    public int find(E e){
        for (int i=0;i<size;i++){
            if (data[i]==e){
                return i;
            }
        }
        return -1;
    }

    public E remove(int index){
        if(index<0 || index>=size){
            throw new IllegalArgumentException("Remove failed. Index is illegal.");
        }
        E ret=data[index];
        for (int i = index+1;i<size;i++){
            data[i-1]=data[i];
        }
        size --;
        data[size]=null;
        return ret;
    }

    public E removeFirst(){
        return remove(0);
    }

    public E removeLast(){
        return remove(size-1);
    }


    public void removeElement(E e){
        int index=find(e);
        if(index!=-1){
            remove(index);
        }
    }

    public E getFirst(){
        return get(0);
    }

    public E getLast(){
        return get(size-1);
    }

}
