package com.example.IqsikNolik.domain;

public enum Symbol {
    X, O, N;

    public static Symbol getSymbol(char ch) {
        return ch == 'N'
                ? N
                : ch == 'X' ? X : O;
    }

}
