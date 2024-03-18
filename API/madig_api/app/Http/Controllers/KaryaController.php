<?php

namespace App\Http\Controllers;

use App\Models\Karya;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;
use App\Http\Requests\StoreKaryaRequest;
use App\Http\Requests\UpdateKaryaRequest;

class KaryaController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    {
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->where('karyas.status','=','Diterima')
        ->orderBy('karyas.judul','asc')
        ->get();

        if ($request->keyword){
            $data = DB::table('karyas')
            ->join('kategoris','kategoris.id','=','karyas.idkategori')
            ->join('penggunas','penggunas.id','=','karyas.idpengguna')
            ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
            ->where('karyas.judul','LIKE','%'.$request->keyword.'%','OR','penggunas.pengguna','LIKE','%'.$request->keyword.'%','OR','kategoris.kategori','LIKE','%'.$request->keyword.'%')
            ->where('karyas.status','=','Diterima')
            ->orderBy('karyas.judul','desc')
            ->get();
        }

        //$data = Karya::all();

        // $data = Auth::pengguna();

        return response()->json($data);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create(Request $request)
    {
        //TO DOS : 1. AUTO MENGENALI ID PENGGUNA:checked
        $this->validate($request, [
            'judul'=>'required',
            'keterangan'=>'required',
            'gambar'=>'required',
            'link'=>'active_url',
            'idkategori'=>'required',
        ]);

        $gambar = $request->file('gambar')->getClientOriginalName();
        $request->file('gambar')->move('karya', $gambar);

        $data = [
            'judul'=>$request->input('judul'),
            'keterangan'=>$request->input('keterangan'),
            'gambar'=>url('karya/'.$gambar),
            'link'=>$request->input('link'),
            'idkategori'=>$request->input('idkategori'),
            'idpengguna'=>$request->input('idpengguna')
        ];

        $run = Karya::create($data);

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil disimpan',
                //'gambar'=>url('karya/'.$gambar),
                'status'=>200,
                'data'=>$data
            ]);
        }
    }


    /**
     * Store a newly created resource in storage.
     *
     * @param  \App\Http\Requests\StoreKaryaRequest  $request
     * @return \Illuminate\Http\Response
     */
    public function store(StoreKaryaRequest $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Karya  $karya
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->where('penggunas.id','=', $id)
        ->orderBy('karyas.judul','asc')
        ->get();

        return response()->json($data);
    }

    public function showKategori($idk){
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->where('karyas.idkategori','=', $idk)
        ->orderBy('karyas.judul','asc')
        ->get();

        return response()->json($data);
    }

    public function showLatest(){
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->orderBy('karyas.tgl_upload','desc')
        ->get();

        return response()->json($data);
    }

    public function showEarliest(){
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->orderBy('karyas.tgl_upload','asc')
        ->get();

        return response()->json($data);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Karya  $karya
     * @return \Illuminate\Http\Response
     */
    public function edit(Karya $karya)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \App\Http\Requests\UpdateKaryaRequest  $request
     * @param  \App\Models\Karya  $karya
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request, [
            'judul'=>'required',
            'keterangan'=>'required',
            'link'=>'active_url'
        ]);

        $data = [
            'judul'=>$request->input('judul'),
            'keterangan'=>$request->input('keterangan'),
            'link'=>$request->input('link')
        ];

        $run = Karya::where('id', $id)->update($data);

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil diperbaharui',
                //'gambar'=>url('karya/'.$gambar),
                'status'=>200,
                'data'=>$data
            ]);
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Karya  $karya
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $run = Karya::where('id', $id)->delete();

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil dihapus',
                'status'=>200
            ]);
        }
    }

    public function cari(){
        $cari = request('cari');
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->where('karyas.judul','like', '%'.$cari.'%')
        ->orderBy('karyas.judul','asc')
        ->get();

        return response()->json($data);
    }
}
