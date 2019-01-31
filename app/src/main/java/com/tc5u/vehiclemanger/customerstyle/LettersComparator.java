package com.tc5u.vehiclemanger.customerstyle;


import java.util.Comparator;

public class LettersComparator implements Comparator<SideBarItemEntity> {
    @Override
    public int compare(SideBarItemEntity o1, SideBarItemEntity o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return 1;
        } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
            return -1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
