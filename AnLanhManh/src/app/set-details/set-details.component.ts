import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';
import { environment } from '../../environments/environment';
import { DOCUMENT } from '@angular/common';
import { UtilService } from '../services/util.service';
import { CartService } from '../services/cart.service';
import { Title } from '@angular/platform-browser';
import { Meta } from '@angular/platform-browser';

@Component({
  selector: 'app-set-details',
  templateUrl: './set-details.component.html',
  styleUrls: ['./set-details.component.css']
})
export class SetDetailsComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    @Inject(DOCUMENT) private document: Document,
    public util: UtilService,
    private cart: CartService,
    private titleService: Title,
    private meta: Meta
  ) { }

  dataCombo: any;

  dataCombo2: any;

  quickViewData: any = null;

  openQuickView(food: any) {
    this.quickViewData = food;
  }

  listProduct: any;
  API_COMBO = `${environment.api_url}/api/combo/`;

  id: number;
  getSet() {
    this.id = this.util.getIDfromURL(this.route.snapshot.params['id']);
    const that = this;
    axios.get(`${that.API_COMBO}${that.id}`).then(function (response) {
      that.dataCombo = response.data.data;
      that.productImage = response.data.data.image;
      that.listProduct = response.data.data.foods;
      that.titleService.setTitle(`${that.dataCombo.name} | AnLanhManh.Com`);
      that.meta.addTags([
        { property: 'og:url', content: window.location.href },
        { property: 'og:type', content: 'article' },
        { property: 'og:title', content: `${that.dataCombo.name} | AnLanhManh.Com` },
        { property: 'og:description', content: that.dataCombo.description },
        { property: 'og:image', content: that.dataCombo.image }
      ]);
    }).catch(function (error) {
      // handle error
      console.log(error);
    });
  }

  public getCombo() {
    const that = this;
    axios.get(`${environment.api_url}/api/combo/list?limit=8`)
      .then(function (response) {
        that.dataCombo2 = response.data.data;
      })
      .catch(function (error) {
        console.log(error);
      })
  }

  addToCart(combo) {
    this.cart.addToCart(combo, 'comboId');
  }

  productImage: any;

  ngOnInit() {
    this.getSet();
    this.getCombo();
  }

  ngAfterViewInit() {
    (function (d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      js = d.createElement(s); js.id = id;
      js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v3.2";

      if (d.getElementById(id)) {
        //if <script id="facebook-jssdk"> exists
        delete (<any>window).FB;
        fjs.parentNode.replaceChild(js, fjs);
      } else {
        fjs.parentNode.insertBefore(js, fjs);
      }
    }(this.document, 'script', 'facebook-jssdk'));
  }
}
