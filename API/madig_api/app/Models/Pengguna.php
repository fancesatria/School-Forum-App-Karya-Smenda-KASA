<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Pengguna extends Model
{
    use HasFactory;

    protected $table = 'penggunas';

    protected $fillable = [
        'pengguna','username','email','password','telp','gambar','token'
    ];
}
