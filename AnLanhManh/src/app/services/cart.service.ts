import { Injectable } from '@angular/core';
import { AppComponent } from '../app.component';
import { ToastrService } from 'ngx-toastr';
import { take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  
  constructor(
    private toastr: ToastrService
  ) { }

  addToCart(product: { id: any; }, type: any) {
    var listCart: any = localStorage.getItem('listCart');
    product['type'] = type;
    product['quantity'] = 1;
    if (listCart == null) {
      listCart = {
        products: [product],
        total: 1
      }
      localStorage.setItem('listCart', JSON.stringify(listCart));
      AppComponent.totalCart = listCart.total;
      this.toastr.success('Xem giỏ hàng và thanh toán', 'Thêm vào giỏ hàng thành công!').onTap.pipe(take(1)).subscribe(() => window.location.href = '/cart');
      return;
      // return {
      //   code: 200,
      //   message: "Xem giỏ hàng và thanh toán"
      // };
    } else {
      listCart = JSON.parse(listCart);
      if (listCart != null && listCart != undefined) {
        var existsItem = false;
        listCart.products.forEach((prd: { id: any; }) => {
          if (prd.id == product.id) {
            existsItem = true;
          }
        });
        if (!existsItem) {
          listCart.products.push(product);
          listCart.total = listCart.products.length;
          localStorage.setItem('listCart', JSON.stringify(listCart));
          AppComponent.totalCart = listCart.total;
          this.toastr.success('Xem giỏ hàng và thanh toán', 'Thêm vào giỏ hàng thành công!').onTap.pipe(take(1)).subscribe(() => window.location.href = '/cart');
          return;
          // return {
          //   code: 200,
          //   message: "Xem giỏ hàng và thanh toán"
          // };
        } else {
          this.toastr.error('Thêm số lượng trong giỏ hàng bạn nhé', 'Oops...');
          return;
          // return {
          //   code: 400,
          //   message: "Thêm số lượng trong giỏ hàng bạn nhé"
          // };
        }
      }
    }
  }
}
