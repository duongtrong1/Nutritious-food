import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor() { }

  formatPrice(price: any) {
    var pri: any = new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
      minimumFractionDigits: 0
    });
    return pri.format(price);
  }

  public getIDfromURL(url): any {
    url = url.replace("/", "");
    url = url.replace(".html", "");
    url = url.split("-");
    return url.pop();
  };

  generateURL(title: any, id: any): any {
    if (title) {
      let str = title;
      str = str.toLowerCase();
      str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
      str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
      str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
      str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
      str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
      str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
      str = str.replace(/đ/g, "d");
      str = str.replace(/!|@|%|\^|\*|\color{#fff}{|}∣|\+|\=|\<|\>|\?|\/|,|\.|\:|\;|\'| |\"|\&|\#|\[|\]|~|$|_/g, "-");

      str = str.replace(/-+-/g, "-");
      str = str.replace(/^\-+|\-+$/g, "");
      str = str.replace(/ /g, "-");
      return `${str}-${id}.html`;
    }
  }

  getTotalCart(arr: { reduce: (arg0: (a: any, b: any) => number, arg1: number) => void; }) {
    return arr.reduce((a, b) => parseInt(a) + (parseInt(b.price) * parseInt(b.quantity)), 0)
  }
}
