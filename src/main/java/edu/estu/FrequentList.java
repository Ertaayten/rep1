package edu.estu;

public class FrequentList<T,F> {
    private T token;
    private F frequent;

    public FrequentList(T token, F frequent){
        this.token = token;
        this.frequent = frequent;

    }
    public T getToken(){
        return token;
    }
    public F getFrequent(){
        return frequent;
    }

    public void print(){
        System.out.println(this.token+ " " + this.frequent);
    }



}
