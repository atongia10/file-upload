package com.gs.tax.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSumProblem {


    public static void main(String[] args) {
        int [] nums = {4,2,4};
        int target = 6;
        System.out.println(Arrays.toString(twoSumProblem(nums, target)));
    }

    private static int[] twoSumProblem(int[] nums, int target) {

        Map<Integer, Integer> map = new HashMap<>();

        for (int i=0;i<nums.length;i++) {
            int compliment = target - nums[i];
            if (map.containsKey(compliment)) {
                return new int [] {map.get(compliment) , i};
            }
            else {
                map.put(nums[i], i);
            }
        }
        return new int[] {};
    }

}
