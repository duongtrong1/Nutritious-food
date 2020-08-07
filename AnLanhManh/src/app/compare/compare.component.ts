import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';
import axios from 'axios';
import { UtilService } from '../services/util.service';
import { CartService } from '../services/cart.service';

@Component({
  selector: 'app-compare',
  templateUrl: './compare.component.html',
  styleUrls: ['./compare.component.css']
})
export class CompareComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    public util: UtilService,
    private cart: CartService
  ) { }

  id: any = null;
  type: any = null;

  firstProduct: any = null;
  secondProduct: any = null;

  listData: any = [];

  @ViewChild('openModal', { static: true }) openModal: ElementRef;
  @ViewChild('closeModal', { static: true }) closeModal: ElementRef;

  isLoading: boolean = false;

  compare(id) {
    const that = this;
    if (id == null) return;
    if (this.isLoading) return;
    this.isLoading = true;
    if (this.type == 'food') {
      axios.get(`${environment.api_url}/api/food/${id}`).then((response) => {
        that.secondProduct = response.data.data;
        that.closeModal.nativeElement.click();
        that.isLoading = false;
      }).catch((err) => {
        console.log(err);
      });
    } else if (this.type == 'combo') {
      axios.get(`${environment.api_url}/api/combo/${id}`).then((response) => {
        that.secondProduct = response.data.data;
        that.closeModal.nativeElement.click();
        that.isLoading = false;
      }).catch((err) => {
        console.log(err);
      });
    } else {
      window.location.href = '/';
      return;
    }
  }

  addToCart(prd, type) {
    this.cart.addToCart(prd, type);
  }

  ngOnInit() {
    const that = this;
    this.id = this.route.snapshot.queryParamMap.get('id');
    this.type = this.route.snapshot.queryParamMap.get('type');
    if (this.id == null || this.type == null) {
      window.location.href = '/';
      return;
    }
    this.openModal.nativeElement.click();
    if (this.type == 'food') {
      axios.get(`${environment.api_url}/api/food/list?limit=1000`).then((response) => {
        that.listData = response.data.data;
      }).catch((err) => {
        console.log(err);
      });
      axios.get(`${environment.api_url}/api/food/${this.id}`).then((response) => {
        that.firstProduct = response.data.data;
      }).catch((err) => {
        console.log(err);
      });
    } else if (this.type == 'combo') {
      axios.get(`${environment.api_url}/api/combo/list?limit=1000`).then((response) => {
        that.listData = response.data.data;
      }).catch((err) => {
        console.log(err);
      });
      axios.get(`${environment.api_url}/api/combo/${this.id}`).then((response) => {
        that.firstProduct = response.data.data;
      }).catch((err) => {
        console.log(err);
      });
    } else {
      window.location.href = '/';
      return;
    }

  }

}
