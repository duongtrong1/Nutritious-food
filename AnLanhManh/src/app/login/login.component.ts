import { Component, OnInit, Inject } from '@angular/core';
import axios from "axios";
import { Router, ActivatedRoute } from "@angular/router";
import { LOCAL_STORAGE, WINDOW } from '@ng-toolkit/universal';
import { environment } from '../../environments/environment';
import { async } from '@angular/core/testing';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(@Inject(WINDOW) private window: Window,
   @Inject(LOCAL_STORAGE) private localStorage: any, 
   private route: ActivatedRoute
  ) {
    
   }

  userProfile: any;

  textError: any = null;

  public handleLogin: Function = async (account: any, password: any) => {
    const that = this;
    if (password.length == 0 || account.length == 0) {
      that.textError = "Tài khoản và mật khẩu là bắt buộc";
    } else {
      that.textError = null;
      await axios.post(`${environment.api_url}/api/auth/signin`, {
        account: account,
        password: password
      }).then(function (response) {
        if (response.data.status == 200) {
          that.localStorage.setItem("token", response.data.accessToken);
          if (that.backURL == null) {
            that.window.location.href = '/';
          } else {
            that.window.location.href = that.backURL;
          }
        }
      }).catch(function (error) {
        if (error.response.data.status == 401) {
          that.textError = error.response.data.message;
        } else {
          that.textError = null;
        }
      });
    }
  }

  backURL: any = null;

  ngOnInit() {
    var token: any = this.localStorage.getItem('token');
    this.backURL = this.route.snapshot.queryParamMap.get("back");
    if (token != null || token != undefined) {
      this.window.location.href = '/';
    }
    //this.getProfile();
  }

}

