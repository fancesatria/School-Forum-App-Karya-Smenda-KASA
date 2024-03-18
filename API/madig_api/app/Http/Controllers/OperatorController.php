<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use App\Models\Karya;
use App\Models\Operator;
use Illuminate\Support\Str;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use App\Http\Requests\StoreOperatorRequest;
use App\Http\Requests\UpdateOperatorRequest;

class OperatorController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $data = Operator::all();

        return response()->json($data);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create(Request $request)
    {
        //validasi
        $this->validate($request, [
            'username'=>'required | alpha_dash | max:255',
            'email'=>'required | string | email | max:255 | unique:admins',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/',
            'telp'=>'required | numeric',
        ]);

        //inisiasi data
        $token = Hash::make(Str::random(80));
        $data = [
            'username'=>$request->input('username'),
            'email'=>$request->input('email'),
            'password'=>Hash::make($request->input('password')),
            'telp'=>$request->input('telp'),
            'token'=>$token
        ];

        //menjalankan data
        $run = Operator::create($data);

        //output
        if($run){
            return response()->json([
                'pesan'=>'Data berhasil disimoan',
                'status'=>200,
                'data'=>$data,
                'token'=>$token
            ]);
        }
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \App\Http\Requests\StoreOperatorRequest  $request
     * @return \Illuminate\Http\Response
     */
    public function store(StoreOperatorRequest $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Operator  $operator
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $data = Operator::where('id',$id)->get();

        return response()->json($data);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Operator  $operator
     * @return \Illuminate\Http\Response
     */
    public function edit(Operator $operator)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \App\Http\Requests\UpdateOperatorRequest  $request
     * @param  \App\Models\Operator  $operator
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request,[
            'username'=>'required | alpha_dash | max:255 | unique:operators',
            'email'=>'required | string | email | max:255 | unique:operators',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/',
            'telp'=>'required | numeric',
        ]);

        $data = [
            'username'=>$request->input('username'),
            'email'=>$request->input('email'),
            'password'=>Hash::make($request->input('password')),
            'telp'=>$request->input('telp')
        ];

        $run = Operator::where('id', $id)->update($data);

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
     * @param  \App\Models\Operator  $operator
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $run = Operator::where('id',$id)->delete();

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil dihapus',
                'status'=>200
            ]);
        }
    }

    public function login(Request $request){
        $this->validate($request, [
            'email'=>'required | string | email | max:255',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/'
        ]);

        $email = $request->input('email');
        $password = $request->input('password');

        //buatmencocokkan data
        $user = Operator::where('email', $email)->first();//ambil data pengguna berdasarkan email
        $token = DB::table('operators')->select('operators.token')
        ->where('email',$email)->first();

        if(isset($user)){
            if($user->status == 1){
                if(Hash::check($password, $user->password)){ //password disandingkan apakah match
                    // sendsms();
                    return response()->json([
                        'pesan'=>'Login Berhasil',
                        'token'=>$user->token, //ini sudah ada di dalam data
                        'data'=>$user
                    ]);

                } else {
                    return response()->json([
                        'pesan'=>'Password salah',
                        'data'=>''
                    ]);
                }

            } else {
                return response()->json([
                    'pesan'=>'Login tak dapat dilakukan karena akun diblokir',
                    'data'=>''
                ]);
            }
        } else {
            return response()->json([
                'pesan'=>'Email tidak ditemukan',
                'data'=>''
            ]);
        }
    }

    public function updateStatusDiterima($idkarya){
        $data = [
            'status'=>'Diterima',
            'tgl_verif'=>Carbon::now()
        ];

        $run = Karya::where('id', $idkarya)->update($data);

        if($run){
            return response()->json([
                'pesan'=>'Status berhasil diubah',
                'data'=>$data
             ]);
             semuaKarya();
        }
    }

    public function updateStatusDitolak($idkarya){
        $data = [
            'status'=>'Ditolak',
            'tgl_tolak'=>Carbon::now(),
        ];

        $run = Karya::where('id', $idkarya)->update($data);
        if($run){
            return response()->json([
                'pesan'=>'Status berhasil diubah',
                'data'=>$data
             ]);
             semuaKarya();
        }
    }

    public function semuaKarya(Request $request){
        $data = Karya::where('status','=','Menunggu Verifikasi')->get();
        if ($request->keyword){
            $data = Karya::where('karyas.judul','LIKE','%'.$request->keyword.'%','OR','penggunas.pengguna','LIKE','%'.$request->keyword.'%','OR','kategoris.kategori','LIKE','%'.$request->keyword.'%')
            ->get();
        }

        return response()->json($data);
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

    public function karyaDiterima(){
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->where('karyas.status','=','Diterima')
        ->orderBy('karyas.created_at','desc')
        ->get();

        return response()->json($data);
    }

    public function karyaDitolak(){
        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->where('karyas.status','=','Ditolak')
        ->orderBy('karyas.created_at','desc')
        ->get();

        return response()->json($data);
    }


}
