import { Component, OnInit, ViewChild, ElementRef, Inject } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { AppComponent } from "../app.component"
import { UtilService } from '../services/util.service';
import axios from 'axios';
import { environment } from 'src/environments/environment';
import { withLatestFrom } from 'rxjs/operators';
import { WINDOW, LOCAL_STORAGE } from '@ng-toolkit/universal';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {

  constructor(
    public util: UtilService,
    @Inject(WINDOW) private window: Window,
    @Inject(LOCAL_STORAGE) private localStorage: any,
    private title: Title
  ) { }

  errorListcart: any = null;

  listCart: any = null;

  listAddress: any;

  listProductOrder: any = null;

  textError: any = null;

  errorOrder: any = null;

  token = this.localStorage.getItem('token');

  addressId: any = [];

  userAddress: any;

  isShowForm = false;

  getTotalPriceCart() {
    return this.util.getTotalCart(this.listCart.products);
  }

  getProduct() {
    const that = this;
    that.listCart = localStorage.getItem('listCart');
    if (that.listCart != null || that.listCart != undefined) {
      that.listCart = localStorage.getItem('listCart');
      that.listCart = JSON.parse(that.listCart);
    } else {
      localStorage.removeItem('listCart');
      that.listCart == null;
      console.log(that.listCart);
    }
  }


  dataTinhThanhPho: any = [];
  dataQuanHuyen: any = [];
  dataPhuongXa: any = [];

  address: any = {
    city: null,
    quanhuyen: null,
    xaphuong: null,
    addressDetails: null,
    type: 1
  }

  currentAddress: any = {
    address: null,
    phone: null,
    note: null
  };

  addressPost: any = {
    title: null
  }

  selectTinhThanhPho(alm) {
    const that = this;
    var code = alm.split('@alm;')[0];
    that.dataQuanHuyen = [];
    that.dataPhuongXa = [];
    axios.get('/resources/data/quan_huyen.json').then(function (response) {
      that.dataQuanHuyen = response.data.filter(qh => {
        return qh.parent_code == code;
      });
    }).catch(function (error) {
      // handle error
      console.log(error);
    });
  }

  selectQuanHuyen(alm) {
    const that = this;
    var code = alm.split('@alm;')[0];
    axios.get('/resources/data/phuong_xa.json').then(function (response) {
      that.dataPhuongXa = response.data.filter(px => {
        return px.parent_code == code;
      });
    }).catch(function (error) {
      // handle error
      console.log(error);
    });
  }

  getAddress() {
    const that = this;
    axios.get(`${environment.api_url}/api/address`, { headers: { Authorization: that.token } })
      .then(function (response) {
        //console.log(response);
        that.listAddress = response.data.data;
        that.currentAddress.address = that.listAddress[0].id;
        //console.log(that.listAddress);
        that.isShowForm = true;
      })
      .catch(function (error) {
        console.log(error);
        that.isShowForm = true;
      })
  }
  @ViewChild('quanhuyen', { static: false }) quanhuyen: ElementRef;
  @ViewChild('xaphuong', { static: false }) xaphuong: ElementRef;
  @ViewChild('city', { static: false }) city: ElementRef;


  //validate form
  payNow() {
    const that = this;
    var token: any = localStorage.getItem('token');
    console.log(that.currentAddress.address);
    if(that.currentAddress.address == null || that.currentAddress.address == ''){
      that.errorOrder = '.';
    }else{
      if(that.currentAddress.phone == null || that.currentAddress.phone == ''){
        that.textError = "Vui lòng nhập số điện thoại"
      }else{
        let obj = {
          addressId: that.currentAddress.address,
          orderDetails: [],
          note: that.currentAddress.note,
          type: that.address.type
        }
        let prm = that.listCart.products.map((prd: { quantity: any; price: any; type: string | number; id: any; }) => {
          new Promise((resolve, reject) => {
            let _obj = {
              foodId: null,
              comboId: null,
              scheduleId: null,
              quantity: prd.quantity
            }
            _obj[prd.type] = prd.id;
            obj.orderDetails.push(_obj);
            resolve();
          });
        });
        Promise.all(prm).then(() => {
          axios.post(`${environment.api_url}/api/order`, obj, { headers: { Authorization: token } }).then((rs) => {
            if (obj.type == 2) {
              window.location.href = rs.data.data.urlPayment;
            } else {
              window.location.href = `/shipping?orderId=${rs.data.data.id}`;
            }
            localStorage.removeItem('listCart');
          }).catch((err) => {
            console.log(err);
          })
          console.log(obj);
        });
        that.localStorage.removeItem('listCart');
        that.errorOrder = null;
  
      }
    }
  }

  addAddr() {
    this.isShowForm = false;
  }

  back() {
    this.isShowForm = true;
    this.textError = null;
  }

  saveAddr() {
    const that = this;
    that.address.city = that.city.nativeElement.value.split('@alm;')[1];
    that.address.quanhuyen = that.quanhuyen.nativeElement.value.split('@alm;')[1];
    that.address.xaphuong = that.xaphuong.nativeElement.value.split('@alm;')[1];
    var check = true;
    for (var key in this.address) {
      if (this.address[key] == null) check = false;
    }
    if (check) {
      that.addressPost = {
        title: `${that.address.addressDetails}, ${that.address.xaphuong}, ${that.address.quanhuyen}, ${that.address.city}`,
        phone: that.currentAddress.phone
      }
      axios.post(`${environment.api_url}/api/address`, that.addressPost, { headers: { Authorization: that.token } })
        .then(function (response) {
          console.log(response);
          that.window.location.href = that.window.location.href;
        })
        .catch(function (error) {
          console.log((error));
        })
    } else {
      that.textError = "Vui lòng nhập đầy đủ thông tin.";
    }
  }

  logAddr() {
    console.log(this.currentAddress.address);
  }

  ngOnInit() {
    this.title.setTitle('Thanh toán | AnLanhManh.Com');
    this.getAddress();
    const that = this;
    this.getProduct();
    if (this.token == null || this.token == undefined) {
      window.location.href = `/login?back=${window.location.pathname}`;
    } else {
      setTimeout(() => {
        var user = this.localStorage.getItem('user');
        if (user != null || user != undefined) {
          user = JSON.parse(user);
          this.currentAddress.phone = user.user.phone;
        }
      }, 2000);
    }
    axios.get('/resources/data/tinh_thanhpho.json').then(function (response) {
      that.dataTinhThanhPho = response.data;
    }).catch(function (error) {
      // handle error
      console.log(error);
    });

    if(localStorage.getItem('listCart') == null || localStorage.getItem('listCart') == ''){
      that.errorListcart = "Vui lòng đặt hàng trước khi tiến tới thanh toán."
    }else{
      that.errorListcart = null;
    }
  }

}
