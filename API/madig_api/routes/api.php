<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AdminController;
use App\Http\Controllers\EventController;
use App\Http\Controllers\KontakController;
use App\Http\Controllers\KategoriController;
use App\Http\Controllers\OperatorController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});
//yg ada middleware buat user aja kalo buat login
//operator dan admin gapake middleware auth
//admin dan operator dibuat group prrefix masing2-
//-biar gampang, gabanyak ketik

Route::prefix('admin')->group(function(){
    //ADMIN AUTH ----------------------
    Route::post('/register', 'AdminController@create');
    Route::post('/login', 'AdminController@login');
    Route::post('/edit/{id}', 'AdminController@update');
    Route::get('/del-auth/{id}', 'AdminController@destroy');
    Route::get('/show/{id}', 'AdminController@show');
    Route::get('/index', 'AdminController@index');
    Route::post('/forgot-password', 'AdminController@forgot');


    //ADMIN KATEGORI ---------------------
    Route::prefix('kategori')->group(function(){
        Route::get('/index', 'KategoriController@index');
        Route::post('/add', 'KategoriController@create');
        Route::post('/edit/{id}', 'KategoriController@update');
        Route::get('/del/{id}', 'KategoriController@destroy');
        Route::get('/show/{id}', 'KategoriController@show');
    });

    //ADMIN KONTAK ---------------------
    Route::prefix('kontak')->group(function(){
        Route::get('/index', 'KontakController@index');
        Route::post('/add', 'KontakController@create');
        Route::post('/edit/{id}', 'KontakController@update');
        Route::get('/del/{id}', 'KontakController@destroy');
        Route::get('/show/{id}', 'KontakController@show');
    });

    //ADMIN EVENT ---------------------
    Route::prefix('event')->group(function(){
        Route::get('/index', 'EventController@index');
        Route::post('/add', 'EventController@create');
        Route::post('/edit/{id}', 'EventController@update');//kalo di put malah error
        Route::get('/del/{id}', 'EventController@destroy');
        Route::get('/show/{id}', 'EventController@show');
        Route::post('/add-picture/{id}', 'EventController@updateGambar');//add img
    });

    //ADMIN KARYA ---------------------
    Route::prefix('karya')->group(function(){
        Route::get('/index', 'AdminController@indexKarya');
        Route::post('/add','KaryaController@create');
        Route::get('/show/{id}','KaryaController@show');//show pengguna
        Route::get('del/{id}','KaryaController@destroy');
        Route::post('/edit/{id}', 'KaryaController@update');
    });
});


Route::prefix('operator')->group(function(){
    //OPERATOR AUTH -------------------
    Route::post('/register', 'OperatorController@create');
    Route::post('/login', 'OperatorController@login');
    Route::post('/edit/{id}', 'OperatorController@update');
    Route::get('/index','OperatorController@index');
    Route::get('/show/{id}','OperatorController@show');
    Route::get('/del-auth/{id}', 'OperatorController@destroy');

    //OPERATOR EVENT ---------------------
    Route::prefix('event')->group(function(){
        Route::get('/index', 'EventController@index');
        Route::post('/add', 'EventController@create');
        Route::post('/edit/{id}', 'EventController@update');//kalo di put malah error
        Route::get('/del/{id}', 'EventController@destroy');
        Route::get('/show/{id}', 'EventController@show');
    });

    //KARYA
    Route::prefix('karya')->group(function(){
        Route::get('/index', 'OperatorController@semuaKarya');
        Route::get('/diterima', 'OperatorController@karyaDiterima');
        Route::get('/ditolak', 'OperatorController@karyaDitolak');
        Route::post('/change-status/ditolak/{idkarya}','OperatorController@updateStatusDitolak');
        Route::post('/change-status/diterima/{idkarya}','OperatorController@updateStatusDiterima');
    });
});


Route::prefix('pengguna')->group(function(){
    //PENGGUNA AUTH -------------------------
    Route::post('/register', 'PenggunaController@create')->name('register');
    Route::post('/login', 'PenggunaController@login')->name('login');
    Route::post('/edit/{id}', 'PenggunaController@update');
    Route::post('/forgot-password', 'PenggunaController@forgot');
    Route::post('/change-profile-picture/{id}', 'PenggunaController@updateGambar');
    Route::get('/index', 'PenggunaController@index');
    Route::get('/show/{id}', 'PenggunaController@show');
    Route::get('/del-auth/{id}', 'PenggunaController@destroy');

    //PENGGUNA KARYA -------------------------------
    Route::prefix('karya')->group(function(){
        Route::post('/add','KaryaController@create');
        Route::post('/edit/{id}', 'KaryaController@update');
        Route::get('/index','KaryaController@index');
        Route::get('/show/{id}','KaryaController@show');//show pengguna
        Route::get('del/{id}','KaryaController@destroy');
        Route::get('show/{idk}','KaryaController@showKategori');
        Route::get('show/latest','KaryaController@showLatest');
        Route::get('show/earliest','KaryaController@showEarliest');
        Route::get('cari','KaryaController@cari');
    });
});

