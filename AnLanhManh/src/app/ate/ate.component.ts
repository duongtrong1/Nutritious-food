import { Component, OnInit } from '@angular/core';
import axios from 'axios';
import { environment } from '../../environments/environment';
import { ActivatedRoute } from '@angular/router';
import { IDropdownSettings } from 'ng-multiselect-dropdown';

@Component({
  selector: 'app-ate',
  templateUrl: './ate.component.html',
  styleUrls: ['./ate.component.css']
})
export class AteComponent implements OnInit {

  constructor(
    private route: ActivatedRoute
  ) { }

  listFood: any[];

  textError: any = null;
  textSuccess: any = null;

  isShowForm = false;

  token: any = null;

  getFood() {
    const that = this;
    axios.get(`${environment.api_url}/api/food/list?limit=1000`)
      .then(function (response) {
        that.listFood = response.data.data;
      })
      .catch(function (error) {
        console.log(error);
      })
  }

  showForm() {
    const that = this;
    that.isShowForm = true;
  }

  back() {
    this.isShowForm = false;
  }

  saveHistory(buaan) {
    const that = this;
    this.textError = null;
    this.textSuccess = null;
    if (that.selectedItems.length == 0) {
      this.textError = "Vui lòng chọn món ăn";
      return;
    };
    let obj = {
      foodIds: that.selectedItems.map((item) => { return item.id }),
      comment: null,
      type: parseInt(buaan)
    }
    axios.post(`${environment.api_url}/api/history`, obj, { headers: { Authorization: `Bearer ${that.token}` } }).then((response) => {
      that.textSuccess = "Lưu thành công";
    }).catch((err) => {
      that.textError = "Có lỗi xảy ra";
      console.log(err);
    });
  }

  selectedItems = [];
  dropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'id',
    textField: 'name',
    enableCheckAll: false,
    itemsShowLimit: 3,
    searchPlaceholderText: 'Tìm món ăn',
    allowSearchFilter: true
  };;
  ngOnInit() {
    const that = this;
    that.token = that.route.snapshot.queryParamMap.get('userId');
    if (that.token == null || that.token == undefined) {
      window.location.href = '/';
      return;
    }
    that.getFood();
  }

}
