import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-contacus',
  templateUrl: './contacus.component.html',
  styleUrls: ['./contacus.component.css']
})
export class ContacusComponent implements OnInit {

  constructor(
    private title: Title
  ) { }

  ngOnInit() {
    this.title.setTitle('Kết nối với AnLanhManh | AnLanhManh.Com');
  }

}
