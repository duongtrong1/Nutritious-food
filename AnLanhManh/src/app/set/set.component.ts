import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import axios from "axios";
import * as _ from 'underscore';
import { environment } from '../../environments/environment';
import { UtilService } from '../services/util.service';
import { CartService } from '../services/cart.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-set',
  templateUrl: './set.component.html',
  styleUrls: ['./set.component.css']
})

export class SetComponent implements OnInit {

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    public util: UtilService,
    private cart: CartService,
    private title: Title
  ) { }

  dataCate: any;
  dataCate2: any;
  dataCombo: any;

  // API_COMBO = `${environment.api_url}/api/combo`;

  pager: any = [{
    limit: null,
    page: null,
    totalItems: null,
    totalPages: null
  }];

  id: any = 1;
  pageOfItems: any = [];
  currentPage: number = 1;
  page: any;
  pages: any = [];
  startPage: number; endPage: number;

  @ViewChild("target", { static: false }) target: ElementRef;

  setCombo(page: number) {
    this.currentPage = page;
    this.router.navigate(['/cl'], { queryParams: { page: page } });
    this.loadCombo(page);
    this.target.nativeElement.scrollIntoView({ block: 'start', behavior: 'smooth', inline: 'nearest' });
  }

  addToCart(c) {
    this.cart.addToCart(c, 'comboId');
  }

  paginationCurrentPage: any = null;
  totalPage: any = null;

  loadMoreData(page) {
    const that = this;
    axios.get(`${environment.api_url}/api/combo/list?page=${page}`).then(function (response) {
      if (response.data.status == 200) {
        var newArr = [...that.dataCombo, ...response.data.data];
        that.dataCombo = newArr;
        that.totalPage = response.data.restPagination.totalPages;
        that.paginationCurrentPage = response.data.restPagination.page;
      }
    }).catch(function (error: any) {
      // handle error
      console.log(error);
    });
  }

  public loadCombo(page: number) {
    const that = this;
    axios.get(`${environment.api_url}/api/combo/list?page=${page}`).then(function (response) {
      that.dataCombo = response.data.data;
      that.totalPage = response.data.restPagination.totalPages;
      that.paginationCurrentPage = response.data.restPagination.page;
    }).catch(function (error) {
      // handle error
      console.log(error);
    });
  }

  ngOnInit() {
    this.page = this.route.snapshot.queryParamMap.get('page');
    if (this.page == null) {
      this.page = 1;
    } else {
      this.page = parseInt(this.page);
    }
    this.currentPage = this.page;
    this.title.setTitle('Set đồ ăn dinh dưỡng | AnLanhManh.Com');
    this.loadCombo(this.page);

    this.loadCategory(this.id);
    // this.chooseCategoryParent(this.id);
    if (this.chooseCategoryParent == this.id) {
      this.chooseProduct = this.id;
    }
  }

  chooseProduct(id){
    const that = this;
    axios.get(`${environment.api_url}/api/combo/category/${id}`)
      .then(function (response) {
          that.dataCombo = response.data.data;
          if (that.dataCombo.length = 0 || that.dataCombo) {
            $('.paginatoin-area').hide();
    
          }
      })
      .catch(function (error: any) {
        // handle error
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
        that.dataCate = response.data.data;
        that.chooseCategoryParent(that.dataCate[0].id);
      })
      .catch(function (error: any) {
        // handle error
        console.log(error);
      });
  }

}
