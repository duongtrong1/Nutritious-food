import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

  constructor(
  ) { }

  token = localStorage.getItem('token');

  isLoading: boolean = true;
  paymentSuccess: boolean = false;

  orderId: any = null;

  ngOnInit() {
    const that = this;
    var url = window.location.href;
    var paramerers = url.split('?')[1];
    if (this.token == null || this.token == undefined) {
      window.location.href = '/login';
      return;
    }
    if (paramerers == undefined) {
      window.location.href = '/';
    } else {
      axios.get(`${environment.api_url}/api/payment/return?${paramerers}`, { headers: { Authorization: this.token } }).then((response) => {
        if (response.data.status == 200) {
          that.orderId = response.data.data.id;
          that.paymentSuccess = true;
        } else {
          that.paymentSuccess = false;
        }
        that.isLoading = false;
      }).catch((err) => {
        console.log(err);
      });
    }
  }

}
