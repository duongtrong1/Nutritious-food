import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import axios from 'axios';
import { environment } from 'src/environments/environment';
import { UtilService } from '../services/util.service';
import { CartService } from '../services/cart.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    public util: UtilService,
    private cart: CartService,
    private title: Title
  ) { }

  listFood: any;

  listCombo: any;

  textError:any = null;

  isFood: boolean = false;

  isCombo:boolean = false;

  addToCart(food, type) {
    this.cart.addToCart(food, type);
  }

  searchFood() {
    const that = this;
    axios.get(`${environment.api_url}/api/food/list?search=${that.keyword}&page=1`)
      .then(function (response) {
        console.log(response);
        that.listFood = response.data.data;
        if(that.listFood == null || that.listFood == '' || that.listFood.length == 0){
          that.textError = "Không tìm thấy sản phẩm nào.";
          that.isFood = true;
        }else{
          that.isFood = false;
        }
      })
      .catch(function (error) {
        console.log(error);
      })
  }

  searchCombo(){
    const that = this;
    axios.get(`${environment.api_url}/api/combo/list?search=${that.keyword}&page=1`)
      .then(function (response) {
        console.log(response);
        that.listCombo = response.data.data;
        if(that.listCombo == null || that.listCombo == '' || that.listCombo.length == 0){
            that.isCombo = true
        }else{
          that.isCombo = false;
        }
      })
      .catch(function (error) {
        console.log(error);
      })
  }
  keyword: any;
  ngOnInit() {
    this.keyword = this.route.snapshot.queryParamMap.get("q");
    if (this.keyword == null || this.keyword == undefined) window.location.href = '/';
    this.title.setTitle(`Kết quả tìm kiếm cho từ khoá "${this.keyword}" | AnLanhManh.Com`);
    this.searchFood();
    this.searchCombo();
  }

}
