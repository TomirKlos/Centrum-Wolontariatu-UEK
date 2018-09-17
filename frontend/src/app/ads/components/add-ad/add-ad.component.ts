import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';
import { SnackBarService } from '../../../shared/snack-bar.service';

import { RequestService } from '../../../requests/shared/request.service';
import {Category} from '../../../shared/interfaces';


@Component({
  selector: 'app-add-ad',
  templateUrl: './add-ad.component.html',
})
export class AddAdComponent implements OnInit {
  formGroup: FormGroup;
  submitButtonDisabled = false;

  categoriesData: String[] = [];

  pathToStaticContent = "http://localhost:8080/static/";
  selectedFile: File[];
  fileHash: any[] = [];


  constructor(private _fb: FormBuilder, private _http: HttpClient, private _sb: SnackBarService, private _requestService: RequestService) {

  }

  ngOnInit() {

    this._requestService.getGroups()
      .subscribe((data: Category[]) => {
        data.forEach(element => {
          this.categoriesData.push(element.name);
        });
      });
      this.formGroup = this._fb.group({
        title: [ '', [ Validators.required ] ],
        description: [ '', [ Validators.required ] ],
        images: [ this.fileHash ],
        categories: [ ],
      });
  }

  submit() {
    this.submitButtonDisabled = true;
    this._http.post(environment.apiEndpoint + '/vAd/', this.formGroup.value).subscribe(
      () => {
        this._sb.open('Oferta została dodana');
      },
      () => this._sb.warning()
    );
  }

  onFileSelected(event) {
    this.selectedFile = <File[]> event.target.files;
    this.onUpload();
  }


  onUpload(){
    const fd = new FormData();
   /* this.selectedFile.forEach((file) =>{
      fd.append('file', file, file.name)
    })*/
    for(var i = 0; i < this.selectedFile.length; i++){
      fd.append('file', this.selectedFile[i], this.selectedFile[i].name)
    }
    this._http.post(environment.apiEndpoint + '/vAd/picture', fd)
    .subscribe(res =>{
      console.log(res);
      (res as string[]).forEach(element => {
        this.fileHash.push(element);
      });
      console.log(this.fileHash)

    })
  }

  deletePicture(doc){
    this.fileHash.forEach( (item, index) => {
      if(item === doc) this.fileHash.splice(index,1);
    });
 }



}
