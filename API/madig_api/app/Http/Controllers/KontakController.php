<?php

namespace App\Http\Controllers;

use App\Models\Kontak;
use Illuminate\Http\Request;
use App\Http\Requests\StoreKontakRequest;
use App\Http\Requests\UpdateKontakRequest;

class KontakController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $data = Kontak::all();

        return response()->json($data);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create(Request $request)
    {
        $this->validate($request, [
            'email'=>'required | string | email| max:255',
            'telp'=>'required | numeric',
            'alamat'=>'required'
        ]);

        $data = [
            'email'=>$request->input('email'),
            'telp'=>$request->input('telp'),
            'alamat'=>$request->input('alamat'),
        ];

        $run = Kontak::create($data);

        // $run = Kontak::create($request->all());

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil disimpan',
                'status'=>200
            ]);
        }
    }


    /**
     * Store a newly created resource in storage.
     *
     * @param  \App\Http\Requests\StoreKontakRequest  $request
     * @return \Illuminate\Http\Response
     */
    public function store(StoreKontakRequest $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Kontak  $kontak
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $data = Kontak::where('id', $id)->get();

        return response()->json($data);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Kontak  $kontak
     * @return \Illuminate\Http\Response
     */
    public function edit(Kontak $kontak)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \App\Http\Requests\UpdateKontakRequest  $request
     * @param  \App\Models\Kontak  $kontak
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request, [
            'email'=>'required | string | email | max:255',
            'telp'=>'required | numeric',
            'alamat'=>'required'
        ]);

        $data = [
            'email'=>$request->input('email'),
            'telp'=>$request->input('telp'),
            'alamat'=>$request->input('alamat'),
        ];

        $run = Kontak::where('id', $id)->update($data);

        // $run = Kontak::where('id', $id)->update($request->all());
 
        if($run){
            return response()->json([
                'pesan'=>'Data berhasil diperbaharui',
                'status'=>200
            ]);
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Kontak  $kontak
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $run = Kontak::where('id', $id)->delete();

        if($run){
            return response()->json([
                'pesan'=>'data berhasil dihapus',
                'status'=>200
            ]);
        }
    }
}
