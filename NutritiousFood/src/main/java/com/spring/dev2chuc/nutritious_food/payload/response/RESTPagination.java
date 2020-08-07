package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RESTPagination {
    private int page;
    private int limit;
    private int totalPages;
    private long totalItems;

    public RESTPagination(int page, int limit, long totalItems) {
        this.page = page;
        this.limit = limit;
        if (limit != 0) {
            this.totalPages = (totalItems % limit == 0) ? (int) (totalItems / limit) : ((int) (totalItems / limit) + 1);
        } else this.totalPages = 0;
        this.totalItems = totalItems;
    }

    public RESTPagination(int page, int limit, int totalPages, long totalItems) {
        this.page = page;
        this.limit = limit;
        if (limit != 0) {
            this.totalPages = (totalItems % limit == 0) ? (int) (totalItems / limit) : ((int) (totalItems / limit) + 1);
        } else this.totalPages = 0;
        this.totalItems = totalItems;
    }

//    public static void main(String[] args) {
//        System.out.println(new Gson().toJson(new RESTPagination(1, 10, 31)));
//    }
}
