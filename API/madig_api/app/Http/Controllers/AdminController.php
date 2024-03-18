<?php

namespace App\Http\Controllers;

use App\Models\Admin;
use App\Models\Karya;
use Illuminate\Support\Str;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class AdminController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $data = Admin::all();

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
            // 'admin'=>'required',
        ]);

        //inisiasi data
        $token = Hash::make(Str::random(80));
        $data = [
            'username'=>$request->input('username'),
            'email'=>$request->input('email'),
            'password'=>Hash::make($request->input('password')),
            'telp'=>$request->input('telp'),
            'token'=>$token
            //yg dibawah ini sama aja
            // 'admin'=>$request->admin,
            // 'username'=>$request->username,
            // 'email'=>$request->email,
            // 'password'=>Hash::make($request->password),
            // 'telp'=>$request->telp,
        ];

        //menjalankan data
        $run = Admin::create($data);

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
     * @param  \App\Http\Requests\StoreAdminRequest  $request
     * @return \Illuminate\Http\Response
     */
    public function store(StoreAdminRequest $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Admin  $admin
     * @return \Illuminate\Http\Response
     */

    public function show($id)
    {
        $data = Admin::where('id',$id)->get();

        return response()->json($data);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Admin  $admin
     * @return \Illuminate\Http\Response
     */
    public function edit(Admin $admin)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \App\Http\Requests\UpdateAdminRequest  $request
     * @param  \App\Models\Admin  $admin
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request,[
            'username'=>'required | alpha_dash | max:255',
            'email'=>'required | string | email | max:255 | unique:admins',
            'telp'=>'required | numeric',
        ]);

        $data = [
            'username'=>$request->input('username'),
            'email'=>$request->input('email'),
            'telp'=>$request->input('telp')
        ];

        $run = Admin::where('id', $id)->update($data);

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
     * @param  \App\Models\Admin  $admin
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $run = Admin::where('id', $id)->delete();

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
        $user = Admin::where('email', $email)->first();//ambil data pengguna berdasarkan email
        $token = DB::table('admins')->select('admins.token')
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

    //forget password
    public function forgot(Request $request){
        $this->validate($request, [
            'username'=>'required | alpha_dash | max:255',
            'email'=>'required | string | email | max:255',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/'
        ]);

        $email = $request->input('email');
        $password = Hash::make($request->input('password'));
        $username = $request->input('username');

        $user = Admin::where('email', $email)->where('username', $username);

        $data = [
            'password'=>$password,
        ];

        $run = Admin::where('email', $email)->where('username', $username)->update($data);

        if($run){
            return response()->json([
                'pesan'=>'Password telah diubah'
            ]);
        } else {
            return response()->json([
                'pesan'=>'Email atau nama salah!'
            ]);
        }
    }

    public function indexKarya(Request $request)
    {
        // $data = DB::table('karyas')
        // ->join('kategoris','kategoris.id','=','karyas.idkategori')
        // ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        // ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        // ->where('karyas.status','=','Diterima')
        // ->orderBy('karyas.judul','asc')
        // ->get();

        $data = DB::table('karyas')
        ->join('kategoris','kategoris.id','=','karyas.idkategori')
        ->join('penggunas','penggunas.id','=','karyas.idpengguna')
        ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
        ->orderBy('karyas.judul','desc')
        ->get();

        if ($request->keyword){
            $data = DB::table('karyas')
            ->join('kategoris','kategoris.id','=','karyas.idkategori')
            ->join('penggunas','penggunas.id','=','karyas.idpengguna')
            ->select('karyas.*','kategoris.kategori','penggunas.id','penggunas.pengguna','penggunas.telp')
            ->where('karyas.judul','LIKE','%'.$request->keyword.'%')
            ->orderBy('karyas.judul','desc')
            ->get();
        }

        //$data = Karya::all();

        // $data = Auth::pengguna();

        return response()->json($data);
    }
}
