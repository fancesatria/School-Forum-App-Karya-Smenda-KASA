<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateKaryasTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('karyas', function (Blueprint $table) {
            $table->id();
            $table->bigInteger('idkategori')->unsigned();
            $table->bigInteger('idpengguna')->unsigned();
            $table->string('judul');
            $table->string('keterangan');
            $table->string('gambar')->nullable();
            $table->date('tgl_upload')->default(now());
            $table->date('tgl_verif')->nullable();
            $table->date('tgl_tolak')->nullable();
            $table->string('status')->default('Menunggu Verifikasi');
            $table->string('link')->nullable();
            $table->Integer('like')->default(0);
            $table->timestamps();

            $table->foreign('idkategori')->references('id')->on('kategoris')->onDelete('cascade');
            $table->foreign('idpengguna')->references('id')->on('penggunas')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('karyas');
    }
}
