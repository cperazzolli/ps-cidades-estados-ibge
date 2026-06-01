package br.com.ibge.cad.container.impl;

import br.com.ibge.cad.container.Container;

import java.util.*;

public class ContainerImpl implements Container {

    private List<Integer> numeros = new ArrayList<>();

    public ContainerImpl() {
    }

    @Override
    public void add(int value) {
     numeros.add(value);
    }

    @Override
    public boolean delete(int value) {
        return numeros.remove(Integer.valueOf(value));

    }

    @Override
    public int getMedian() {
        Collections.sort(numeros);

        int tamanho = numeros.size();
        if (tamanho % 2 == 0) {
            return numeros.get((tamanho / 2) - 1);
        } else {
            return numeros.get(tamanho / 2);
        }
    }
}
