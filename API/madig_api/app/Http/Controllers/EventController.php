<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use App\Models\Event;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Http\Requests\StoreEventRequest;
use App\Http\Requests\UpdateEventRequest;

class EventController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    {
        $data = Event::all();
        if ($request->keyword){
            $data = Event::where('judul','LIKE','%'.$request->keyword.'%')->get();
        }

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
            'judul'=>'required',
            'deskripsi'=>'required',
        ]);

        $data = [
            'judul'=>$request->input('judul'),
            'deskripsi'=>$request->input('deskripsi'),
        ];

        $run = Event::create($data);
        //return response()->json([200]);

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil disimpan',
                'status'=>200,
                'data'=>$data
            ]);
        }
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \App\Http\Requests\StoreEventRequest  $request
     * @return \Illuminate\Http\Response
     */
    public function store(StoreEventRequest $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Event  $event
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $data = Event::where('id', $id)->get();

        return response()->json($data);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Event  $event
     * @return \Illuminate\Http\Response
     */
    public function edit(Event $event)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \App\Http\Requests\UpdateEventRequest  $request
     * @param  \App\Models\Event  $event
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request, [
            'judul'=>'required',
            'deskripsi'=>'required'
        ]);


        $data = [
            'judul'=>$request->input('judul'),
            'deskripsi'=>$request->input('deskripsi')
        ];

        $run = Event::where('id', $id)->update($data);

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil diperbaharui',
                'status'=>200,
                'data'=>$data
            ]);
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Event  $event
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $run = Event::where('id', $id)->delete();

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil dihapus',
                'status'=>200
            ]);
        }
    }

    //update gambar
    public function updateGambar(Request $request, $id){
        $this->validate($request, [
            'gambar'=>'required'
        ]);

        // $email = Pengguna::where('id', $id)->first();
        $email = str_replace(',','.',$id);
        $gambar = $request->file('gambar')->getClientOriginalName();
        $request->file('gambar')->move('upload/'. $gambar);

        $data = [
            //acces pubic path image
            'gambar'=>url('upload/'.$gambar)
        ];

        // return response()->json($gambar);
        // return response()->json($data);

        // $run = Pengguna::where('email', $email)->update($data);
        $run = Event::where('id',$id)->update($data);

        if($run){
            return response()->json([
                'gambar'=>url('upload/'.$gambar),
                'pesan'=>'Data telah disimpan'
            ]);
        }
    }
}
