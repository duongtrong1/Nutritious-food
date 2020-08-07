import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from "axios";
import { WINDOW } from '@ng-toolkit/universal';
import { CartService } from '../services/cart.service';
import { environment } from '../../environments/environment';
import { DOCUMENT, Location } from '@angular/common';
import { UtilService } from '../services/util.service';
import { Title, Meta } from '@angular/platform-browser';


@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  dataFood: any;

  constructor(
    private route: ActivatedRoute,
    private cart: CartService,
    private location: Location,
    @Inject(DOCUMENT) private document: Document,
    public util: UtilService,
    private titleService: Title,
    private meta: Meta
  ) {
  }

  currentTab: any = 1;

  changeTab(tab) {
    this.currentTab = tab;
  }

  foodInCate: any = [];
  id: number;

  addToCart(product) {
    this.cart.addToCart(product, 'foodId');
  }

  currentURL: any;

  ngOnInit() {
    this.currentURL = this.location.path();
    this.id = this.util.getIDfromURL(this.route.snapshot.params['id']);
    const that = this;
    axios.get(`${environment.api_url}/api/food/${this.id}`).then(function (response: any) {
      if (response.data.status == 200) {
        that.dataFood = response.data.data;
        that.titleService.setTitle(`${that.dataFood.name} | AnLanhManh.Com`);
        that.meta.addTags([
          { property: 'og:url', content: window.location.href },
          { property: 'og:type', content: 'article' },
          { property: 'og:title', content: `${that.dataFood.name} | AnLanhManh.Com` },
          { property: 'og:description', content: that.dataFood.description },
          { property: 'og:image', content: that.dataFood.image }
        ]);
        axios.get(`${environment.api_url}/api/food/category/${that.dataFood.categories[0].id}`)
          .then(function (response) {
            that.foodInCate = response.data.data;
          })
          .catch(function (error) {
            console.log(error);
          })
      }
    }).catch(function (error: any) {
      // handle error
      console.log(error);
    });
  }

  ngAfterViewInit() {
    (function (d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      js = d.createElement(s); js.id = id;
      js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v3.2";

      if (d.getElementById(id)) {
        delete (<any>window).FB;
        fjs.parentNode.replaceChild(js, fjs);
      } else {
        fjs.parentNode.insertBefore(js, fjs);
      }
    }(this.document, 'script', 'facebook-jssdk'));
  }

}
