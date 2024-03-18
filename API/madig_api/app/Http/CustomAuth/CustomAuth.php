<?php

use Illuminate\Support\Facades\Auth;
namespace Illuminate\Support\Facades;

 class CustomAuth extends Auth
 {
    public static function pengguna(){
        return 'pengguna';
    }
 }
