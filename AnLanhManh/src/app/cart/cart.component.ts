import { Component, OnInit, Inject } from '@angular/core';
import { CartService } from '../services/cart.service';
import { ToastrService } from 'ngx-toastr';
import { UtilService } from '../services/util.service';
import { AppComponent } from '../app.component';
import axios from 'axios';
import { LOCAL_STORAGE, WINDOW } from '@ng-toolkit/universal';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  constructor(
    private cart: CartService,
    private toastr: ToastrService,
    public util: UtilService,
    @Inject(WINDOW) private window: Window,
    @Inject(LOCAL_STORAGE) private localStorage: any,
    private title: Title
  ) { }

  listCart: any = null;

  textError: any = null;

  token: any = this.localStorage.getItem('token');

  getTotalPriceCart() {
    return this.util.getTotalCart(this.listCart.products);
  }

  removeAllItem() {
    localStorage.removeItem('listCart');
    this.listCart = null;
    this.toastr.success('Đã xoá trống giỏ hàng', 'Thông báo!');
  }

  totalPrice(qtt: string, price: string) {
    return parseInt(price) * parseInt(qtt);
  }

  sumItem(qtt: string, id: any) {
    this.listCart.products.forEach((prd: { id: any; quantity: number; }) => {
      if (prd.id == id) {
        prd.quantity = parseInt(qtt);
      };
    });
    localStorage.setItem('listCart', JSON.stringify(this.listCart));
  }

  removeItem(id: any) {
    this.listCart.products = this.listCart.products.filter((prd: { id: any; }) => {
      return prd.id != id;
    });
    this.listCart.total = this.listCart.products.length;
    AppComponent.totalCart = this.listCart.total;
    if (this.listCart.total == 0) {
      localStorage.removeItem('listCart');
      this.listCart = null;
    } else {
      localStorage.setItem('listCart', JSON.stringify(this.listCart));
    }
    this.toastr.success('Đã xoá sản phẩm khỏi giỏ hàng', 'Thông báo!');
  }

  checkLogin() {
    this.window.location.href = '/order';
  }

  ngOnInit() {
    this.listCart = localStorage.getItem('listCart');
    this.listCart = JSON.parse(this.listCart);
    this.title.setTitle('Giỏ hàng | AnLanhManh.Com');
  }

}
