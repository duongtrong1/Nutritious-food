import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import axios from "axios";
import * as _ from 'underscore';
import { environment } from '../../environments/environment';
import { UtilService } from '../services/util.service';
import { CartService } from '../services/cart.service';
import { empty } from 'rxjs';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  dataFood: any;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    public util: UtilService,
    private cart: CartService,
    private title: Title
  ) {
  }

  pager: any = [{
    limit: null,
    page: null,
    totalItems: null,
    totalPages: null
  }];

  pageOfItems: any;
  currentPage: number = 1;
  page: any;
  pages: any = [];
  startPage: number; endPage: number;


  dataCate: any;
  dataCate2: any;
  id: any = 1;
  @ViewChild("target", { static: false }) target: ElementRef;

  setPage(page: number) {
    if (page < 1 || page > this.pager.totalPages) {
      return;
    }
    this.currentPage = page;
    this.router.navigate(['/pl/list'], { queryParams: { page: page } });
    this.loadPage(page);
    this.target.nativeElement.scrollIntoView({ block: 'start', behavior: 'smooth', inline: 'nearest' });

  }

  ngOnInit() {
    this.page = this.route.snapshot.queryParamMap.get('page');
    if (this.page == null) {
      this.page = 1;
    } else {
      this.page = parseInt(this.page);
    }
    this.currentPage = this.page;
    this.title.setTitle('Đồ ăn dinh dưỡng | AnLanhManh.Com');
    this.loadPage(this.page);

    this.loadCategory(this.id);
    if (this.chooseCategoryParent == this.id) {
      this.chooseProduct = this.id;
    }
  }

  addToCart(food) {
    this.cart.addToCart(food, 'foodId');
  }

  chooseProduct(id) {
    const that = this;
    axios.get(`${environment.api_url}/api/category/${id}`).then(function (response) {
      that.dataFood = response.data.data.foods;
      if (that.dataFood.length = 0 || that.dataFood) {
        $('.paginatoin-area').hide();

      }
    }).catch(function (error: any) {
      console.log(error);
    });
  }

  childCategory: any;

  chooseCategoryParent(id) {
    const that = this;
    axios.get(`${environment.api_url}/api/category/parent/${id}`)
      .then(function (response) {
        if (that.childCategory = id) {
          that.dataCate2 = response.data.data;
        }
      })
      .catch(function (error: any) {
        // handle error
        console.log(error);
      });
  }

  loadCategory(id) {
    const that = this;
    axios.get(`${environment.api_url}/api/category/parent/${id}`)
      .then(function (response) {
        if (response.data.status == 200) {
          that.dataCate = response.data.data;
          that.chooseCategoryParent(that.dataCate[0].id);
        }
      })
      .catch(function (error: any) {
        // handle error
        console.log(error);
      });
  }

  paginationCurrentPage: any = null;
  totalPage: any = null;

  loadMoreData(page: any) {
    const that = this;
    axios.get(`${environment.api_url}/api/food/list?page=${page}`).then(function (response) {
      if (response.data.status == 200) {
        var newArr = [...that.dataFood, ...response.data.data];
        that.dataFood = newArr;
        that.totalPage = response.data.restPagination.totalPages;
        that.paginationCurrentPage = response.data.restPagination.page;
      }
    }).catch(function (error: any) {
      // handle error
      console.log(error);
    });
  }

  loadPage(page: number) {
    const that = this;
    axios.get(`${environment.api_url}/api/food/list?page=${page}`).then(function (response) {
      if (response.data.status == 200) {
        that.dataFood = response.data.data;
        that.totalPage = response.data.restPagination.totalPages;
        that.paginationCurrentPage = response.data.restPagination.page;
      }
    }).catch(function (error: any) {
      // handle error
      console.log(error);
    });
  }
}
