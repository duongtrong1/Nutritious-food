import { Component, OnInit, Inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import axios from "axios";
import { CartService } from '../services/cart.service';
import { environment } from '../../environments/environment';
import { UtilService } from '../services/util.service';
import { WINDOW, LOCAL_STORAGE } from '@ng-toolkit/universal';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  dataFood: any = [];

  userDetails: any;

  dataCombo: any = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private cart: CartService,
    public util: UtilService,
    @Inject(WINDOW) private window: Window,
    @Inject(LOCAL_STORAGE) private localStorage: any,
    private title: Title
  ) { }

  pager: any = [{
    limit: null,
    page: null,
    totalItems: null,
    totalPages: null
  }];

  token: any = this.localStorage.getItem('token');

  dataCate: any = [{
    id: null,
    name: null,
    parentId: null
  }];

  outProductTab: any = null;

  outProductData: any = [];

  public chooseTabOutProduct: Function = (tab, id) => {
    this.getProductById(id);
    this.outProductTab = tab;
  }

  pageOfItems: any = [];
  currentPage: number = 1;
  page: any;
  pages: any = [];

  setPage(page: number) {
    this.currentPage = page;
    this.router.navigate(['/pl/list'], { queryParams: { page: page } });
  }

  getProductById(id: any) {
    const that = this;
    that.dataFood = null;
    axios.get(`${environment.api_url}/api/category/${id}`).then(function (response) {
      that.dataFood = response.data.data.foods;
    }).catch(function (error: any) {
      console.log(error);
    });
  }

  getCombo(){
    const that = this;
    axios.get(`${environment.api_url}/api/combo`)
    .then(function (response){
      that.dataCombo = response.data.data;
    })
    .catch(function (error){
      console.log(error);
    })
  }

  

  ngOnInit() {
    this.title.setTitle('AnLanhManh.Com | Safe, Clean and Nutritious')
    this.getCombo();
    this.page = this.route.snapshot.queryParamMap.get('page');
    if (this.page == null) {
      this.page = 1;
    } else {
      this.page = parseInt(this.page);
    }
    this.currentPage = this.page;
    const that = this;
    axios.get(`${environment.api_url}/api/category/parent/8`).then(function (response) {
      var data = response.data.data;
      data.forEach((cate: { id: any; name: any; }) => {
        let obj = {
          id: cate.id,
          name: cate.name,
          tab: `tab_${cate.id}`,
          groupId: cate.id
        };
        that.outProductData.push(obj);
      });
      var currentId = data[0].id;
      that.outProductTab = `tab_${currentId}`;
      that.getProductById(currentId);
    }).catch(function (error) {
      console.log(error);
    });
  }

  addToCart(food, type) {
    this.cart.addToCart(food, type);
    //console.log(food);
  }

  foodDetail(id: number) {
    this.router.navigate(["/product-detail/food", id]);
  }

}
