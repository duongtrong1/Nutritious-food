import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';
import { environment } from '../../environments/environment';
import { UtilService } from '../services/util.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-shipping',
  templateUrl: './shipping.component.html',
  styleUrls: ['./shipping.component.css']
})
export class ShippingComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    public util: UtilService,
    private title: Title
  ) { }

  token = localStorage.getItem('token');

  orderData: any = null;

  ngOnInit() {
    this.title.setTitle('Thông tin đơn hàng | AnLanhManh.Com');
    const that = this;
    var orderId = this.route.snapshot.queryParamMap.get('orderId');
    if (orderId == null || orderId == undefined) {
      window.location.href = '/';
      return;
    }
    axios.get(`${environment.api_url}/api/order/${orderId}`, { headers: { Authorization: that.token } }).then((response) => {
      that.orderData = response.data.data;
    }).catch((err) => {
      console.log(err);
    })
  }

}
