<?php

namespace App\Models;

use App\Models\Pengguna;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Karya extends Model
{
    use HasFactory;

    protected $table = 'karyas';

    protected $fillable = [
        'judul','keterangan','gambar','tgl_upload','like','idkategori','idpengguna','idadmin','status','tgl_verif','tgl_tolak'
    ];

    public function penciptaKarya(){
        return $this->hasOne(Pengguna::class);
    }


}
