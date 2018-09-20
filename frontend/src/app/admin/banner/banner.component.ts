import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';

import { SnackBarService } from '../../shared/snack-bar.service';
import {User, Category, Banner} from '../../shared/interfaces';
import { BannerService } from './banner.service';
import { environment } from '../../../environments/environment';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-banner',
  templateUrl: './banner.component.html',
  styleUrls: [ './banner.component.scss' ],
  providers: [
    BannerService
  ]
})
export class BannerComponent implements OnInit, AfterViewInit {
  columnsToDisplay = [ 'title', 'thumbnail', 'up', 'down', 'delete' ];
  formGroup: FormGroup;
  totalElements: number;
  addBannerShow: boolean = false;

  bannerToAdd: BannerImpl = new BannerImpl;
  banners: Banner[] = [];

  pathToStaticContent = "http://localhost:8080/static/";
  selectedFile: File[];
  fileHash: any[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _fb: FormBuilder, private _http: HttpClient, private _bannerService: BannerService, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.formGroup = this._fb.group({
      title: [ '', [ Validators.required ] ],
      description: [ '', [ Validators.required ] ],
      referenceToPicture: [ this.fileHash ],
    });

    this._loadBannerPage();

    this._bannerService.getPage().subscribe(d => {
      if (d && d.totalElements) {
        this.totalElements = d.totalElements;
      }
    });
  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this._loadBannerPage())
      )
      .subscribe();
  }

  _loadBannerPage() {
    this._bannerService.getBanners()
      .subscribe((data: Banner[]) => {
        this.banners = data;
      });
  }

  /*deleteCategory(name: string) {
    this._categoriesService.deleteCategory(name).subscribe(() => this._loadCategoryPage());
  }*/

  addBanner() {
    this.addBannerShow = !this.addBannerShow;
  }

  createBanner(): void {
    this._bannerService.createBanner(this.bannerToAdd)
      .subscribe( data => {
        alert('Baner został dodany');
        this._loadBannerPage();
      });
  }

  onFileSelected(event) {
    this.selectedFile = <File[]> event.target.files;
    this.onUpload();
  }


  onUpload(){
    const fd = new FormData();
    for (var i = 0; i < this.selectedFile.length; i++) {
      fd.append('file', this.selectedFile[i], this.selectedFile[i].name);
    }
    this._http.post(environment.apiEndpoint + '/carouselbanner/picture', fd)
      .subscribe(res => {
        console.log(res);
        (res as string[]).forEach(element => {
          this.fileHash.push(element);
        });
        console.log(this.fileHash);

      });
  }

  submit(){
    this._http.post(environment.apiEndpoint + '/carouselbanner/', this.formGroup.value).subscribe(
      () => {
        this._sb.open('Baner został dodany');
        this._loadBannerPage();
      },
      () => this._sb.warning()
    );
  }
  deleteCategory(id: number) {
    this._bannerService.delete(id).subscribe(() => this._loadBannerPage());
  }
  deletePicture(doc){
    this.fileHash.forEach( (item, index) => {
      if(item === doc) this.fileHash.splice(index,1);
    });
  }

  makeBannerUp(id: number) {
    this._bannerService.makeBannerUp(id).subscribe( () => this._loadBannerPage());
  }

  makeBannerDown(id: number) {
    this._bannerService.makeBannerDown(id).subscribe( () => this._loadBannerPage());
  }

}
export class BannerImpl {
  description: string;
  title: string;
  referenceToPicture: string;
}
