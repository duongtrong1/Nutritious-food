import { Component, OnInit, Inject, ViewChild, ElementRef } from '@angular/core';
import axios from 'axios';
import { LOCAL_STORAGE, WINDOW } from '@ng-toolkit/universal';
import { environment } from '../../environments/environment';
import { AppComponent } from '../app.component';
import { UtilService } from '../services/util.service';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../services/cart.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(@Inject(WINDOW) private window: Window,
    @Inject(LOCAL_STORAGE) private localStorage: any,
    public util: UtilService,
    private title: Title,
    private cart: CartService,
    private route: ActivatedRoute) {
  }

  token: any = this.localStorage.getItem('token');

  listProductOrder: any[];

  stt = 0;

  bmi: any;

  isHistory: boolean = false;

  listFoodHistory: any = [];

  bodyStatus: any;

  isShowForm: boolean = false;

  editAddress: boolean = false;

  userDetails: any;

  userAddress: any;

  listAddress: any = [];

  myInfo: any;

  listCart: any = null;

  textError: any = null;

  API_PROFILE = `${environment.api_url}/api/user-profile`;

  API_GETME = `${environment.api_url}/api/auth/me`;

  API_CHANGEPASSWORD = `${environment.api_url}/api/auth/password/change`;

  dataTinhThanhPho: any = [];
  dataQuanHuyen: any = [];
  dataPhuongXa: any = [];

  address: any = {
    city: null,
    quanhuyen: null,
    xaphuong: null,
    addressDetails: null
  }

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

  @ViewChild('quanhuyen', { static: false }) quanhuyen: ElementRef;
  @ViewChild('xaphuong', { static: false }) xaphuong: ElementRef;
  @ViewChild('city', { static: false }) city: ElementRef;

  public getMe: Function = () => {
    const that = this;
    axios.get(that.API_GETME, { headers: { Authorization: that.token } }).then(function (response) {
      that.myInfo = response.data.data;
      //that.getProfile(`${that.API_PROFILE}/${that.myInfo.id}`);
      //console.log(that.myInfo);
    }).catch(function (error) {
      // handle error
      console.log(error);
    });
  }

  getLatestProfile() {
    const that = this;
    axios.get(`${environment.api_url}/api/user-profile/latest`, { headers: { Authorization: that.token } })
      .then(function (response) {
        //console.log(response.data.data);
        that.userDetails = response.data.data;
        that.bmi = that.userDetails.bmiIndex;
      })
      .catch(function (error) {
        console.log(error);
      })
  }

  public changePassword: Function = async (oldPassword: any, password: any, confirmPassword: any) => {
    const that = this;
    if (oldPassword.length == 0 || password.length == 0 || confirmPassword.length == 0) {
      that.textError = "Vui lòng nhập thông tin"
    } else if (password.length < 6) {
      that.textError = "Mật khẩu phải lớn hơn 6 kí tự!"
    } else if (password.length != confirmPassword.length) {
      that.textError = "Mật khẩu không trùng nhau !"
    } else {
      that.textError = null;
      axios.put(that.API_CHANGEPASSWORD,
        {
          oldPassword: oldPassword,
          password: password,
          confirmPassword: confirmPassword
        }, { headers: { Authorization: that.token } }).then(function (response) {
          if (response.data.status == 200) {
            that.window.location.href = '/profile';
          }
          console.log(response)
        }).catch(function (error) {
          if (error.response.data.status == 401) {
            that.textError = error.response.data.message;
          } else {
            that.textError = null;
          }
        });
    }

  }

  reOrder(listPrd: { map: (arg0: (prd: any) => void) => void; }) {
    listPrd.map((prd) => {
      if (prd.food) {
        this.cart.addToCart(prd.food, 'foodId');
        return;
      }
      if (prd.combo) {
        this.cart.addToCart(prd.combo, 'comboId');
      }
    });

  }

  detailModal: any = null;

  openDetail(dataDetail) {
    this.detailModal = dataDetail;
    localStorage.setItem('detailModal', JSON.stringify(this.detailModal));
  }

  getListProductOrder() {
    const that = this;
    axios.get(`${environment.api_url}/api/order`, { headers: { Authorization: that.token } })
      .then(function (response) {
        //console.log(response);
        that.listProductOrder = response.data.data;
        //console.log(that.listProductOrder);
        if (that.listProductOrder == null || that.listProductOrder.length == 0) {
          that.isShowForm = true;
        } else {
          that.isShowForm = false;
        }

      })
      .catch(function (error) {
        console.log(error);
      })
  }

  getHistory(){
    const that = this;
    axios.get(`${environment.api_url}/api/history`, {headers : { Authorization : that.token}})
    .then(function (response){
      console.log(response);
      that.listFoodHistory = response.data.data;
      if(that.listFoodHistory.length == 0){
        that.isHistory = true;
        return;
      }
      that.isHistory = false;
    })
    .catch(function (error){
      console.log(error);
      
    })
  }

  goHome() {
    this.window.location.href = '/'
  }

  getStatus(stt) {
    switch (stt) {
      case 1:
        return "<span class='text-warning'>Chờ xác nhận</span>";
      case 2:
        return "<span class='text-success'>Đã xác nhận</span>";
      case 3:
        return "<span class='text-warning'>Đang giao</span>";
      case 4:
        return "<span class='text-success'>Giao thành công</span>";
      case 5:
        return "<span class='text-danger'>Giao thất bại</span>";
      default:
        break;
    }
  }

  getStatus2(stt){
    const that = this;
    if(that.bmi < 18.5){
      return "<span class='text-warning'>"+ that.bmi + " (Gầy)</span>";
    }else if(that.bmi > 18.5 && that.bmi < 23){
      return "<span class='text-success'>"+ that.bmi +" (Bình thường)</span>";
    }else if(that.bmi > 23 && that.bmi < 25){
      return "<span class='text-warning'>"+ that.bmi +" (Thừa cân)</span>";
    }else if(that.bmi > 25 && that.bmi < 30){
      return "<span class='text-danger'>"+ that.bmi + "(Béo phì cấp độ 1)</span>";
    }else if(that.bmi > 30){
      return "<span class='text-danger'>"+ that.bmi +" (Béo phì cấp độ 2)</span>";
    }
  }

  logout() {
    localStorage.clear();
  }

  ngOnInit() {
    if (this.token == null || this.token == undefined) {
      window.location.href = '/login'
    }
    this.title.setTitle('Trang cá nhân | AnLanhManh.Com');
    const that = this;
    this.getMe();
    this.getLatestProfile();
    axios.get('/resources/data/tinh_thanhpho.json').then(function (response) {
      that.dataTinhThanhPho = response.data;
    }).catch(function (error) {
      // handle error
      console.log(error);
    });
    this.getListProductOrder();
    this.getHistory();
  }
}
