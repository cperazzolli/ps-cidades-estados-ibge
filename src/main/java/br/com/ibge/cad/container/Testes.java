package br.com.ibge.cad.container;

import java.util.HashMap;
import java.util.Map;

public class Testes {

    public static void main(String[] args) {
        System.out.println(codeSignal1());
    }

    public static int codeSignal1() {
        int[] a = {2,4,7};

        int indexOfMinimum = -1;
        int minimalSum = Integer.MAX_VALUE;

        for (int i = 0; i < a.length; i++) {
            int sum = 0;
            for (int j = 0; j < a.length; j++) {
                sum += Math.abs(a[j] - a[j]);
            }
            if (sum < minimalSum) {
                minimalSum = sum;
                indexOfMinimum = i;
            }
        }

        return a[indexOfMinimum];
    }



    public static boolean anagrama() {
        String amor = "amor";
        String roma = "roma";

        if (amor.length() != roma.length()) {
            return false;
        }
        HashMap<Character, Integer> mapa = new HashMap<>();
        for(char c : amor.toCharArray()) {
            mapa.put(c, mapa.getOrDefault(c, 0) + 1);
        }

        for(char c : roma.toCharArray()) {
            if(!mapa.containsKey(c)) {
                return false;
            }
            mapa.put(c, mapa.get(c) - 1);
            if(mapa.get(c) == 0) {
                mapa.remove(c);
            }
        }
        return mapa.isEmpty();
    }
    public static void palavarasRepetidas() {
        String frase = "java spring java kafka java redis spring";
        String[] palavras = frase.split(" ");
        Map<String, Integer> palavrasMap = new HashMap<>();
        for (String palavra : palavras) {
            palavrasMap.put(
                    palavra,
                    palavrasMap.getOrDefault(palavra, 0) + 1);
        }
        palavrasMap.entrySet()
                .stream().sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

    }

    public static int desafioString1(){
        String s = "abcabcde";
        int maxLength = 0;
        for(int right = 0,left =0; s.length() > right; right++) {
            int indexOfFirstAppearenceInSubstring = s.indexOf(s.charAt(right),left);
            if (indexOfFirstAppearenceInSubstring != right) {
                left = indexOfFirstAppearenceInSubstring + 1;
            }

            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    public static int desafioString0() {
        String s = "abcabcde";
        int maxLength = 0;
        for(int i = 0; i < s.length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int j = i; j < s.length(); j++) {
                if (stringBuilder.indexOf(String.valueOf(s.charAt(j))) != -1) {
                    break;
                }
                stringBuilder.append(s.charAt(j));
                maxLength = Math.max(maxLength, stringBuilder.length());
            }

        }
        System.out.println(maxLength);
        return maxLength;
    }

    public static int[] calculateTarget() {
        int[] nums = {5,2,4};
        int target = 6;
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            Integer complementIndex = map.get(nums[i]);
            if(complementIndex != null) {
                return new int[] {complementIndex,i};
            }
            map.put(target - nums[i],i);
        }
        return nums;
    }


    public static void hashMap1() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("maca",3.00);
        map.put("banana",2.00);
        map.put("manga",10.00);
        map.put("mamao",1.00);
        map.put("pera",5.00);

        System.out.println(map.containsKey("maca"));
        System.out.println(map.containsValue(3.00));
        System.out.println(map.size());
    }
}
