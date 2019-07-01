package com.example.hussnain.girovado;

import java.util.ArrayList;
import java.util.List;

public class ListFoundLeisure {
    private static List<FoundLeisure> leisures = new ArrayList<>();
    ListFoundLeisure(List<FoundLeisure> leisures) {
        this.leisures = leisures;
    }

    public static List<FoundLeisure> getLeisures() {
        return leisures;
    }
}
