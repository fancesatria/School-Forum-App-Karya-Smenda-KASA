<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use App\Http\Requests\StoreUserRequest;
use App\Http\Requests\UpdateUserRequest;

class UserController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $data = User::all();

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
            'User'=>'required | alpha_dash | max:255 | unique:Users',
            'email'=>'required | string | email | max:255 | unique:Users',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/',
            'telp'=>'required | numeric',
        ]);

        //di if karena gambarnya nullable
        if($request->hasFile('gambar')){
            $gambar = $request->file('gambar')->getClientOriginalName();
            $request->file('gambar')->move('upload', $gambar);

            $data = [
                'User'=>$request->input('User'),
                'email'=>$request->input('email'),
                'password'=>Hash::make($request->input('password')),
                'telp'=>$request->input('telp'),
                'gambar'=>url('upload/'.$gambar)
            ];
        } else {
            $data = [
                'User'=>$request->input('User'),
                'email'=>$request->input('email'),
                'password'=>Hash::make($request->input('password')),
                'telp'=>$request->input('telp')
            ];
        }



        $run = User::create($data);

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
     * @param  \App\Http\Requests\StoreUserRequest  $request
     * @return \Illuminate\Http\Response
     */
    public function store(StoreUserRequest $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\User  $User
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $data = User::where('id',$id)->get();


        return response()->json($data);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\User  $User
     * @return \Illuminate\Http\Response
     */
    public function edit(User $User)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \App\Http\Requests\UpdateUserRequest  $request
     * @param  \App\Models\User  $User
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request, [
            'User'=>'required | alpha_dash | max:255 | unique:Users',
            'email'=>'required | string | email | max:255 | unique:Users',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/',
            'telp'=>'required | numeric',
        ]);

        if($request->hasFile('gambar')){
            $gambar = $request->file('gambar')->getClientOriginalName();
            $request->file('gambar')->move('upload', $gambar);

            $data = [
                'User'=>$request->input('User'),
                'email'=>$request->input('email'),
                'password'=>Hash::make($request->input('password')),
                'telp'=>$request->input('telp'),
                'gambar'=>url('upload/'.$gambar)
            ];

        } else {
            $data = [
                'User'=>$request->input('User'),
                'email'=>$request->input('email'),
                'password'=>Hash::make($request->input('password')),
                'telp'=>$request->input('telp')
            ];
        }

        $run = User::where('id', $id)->update($data);

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
     * @param  \App\Models\User  $User
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $run = User::where('id',$id)->delete();

        if($run){
            return response()->json([
                'pesan'=>'Data berhasil dihapus',
                'status'=>200
            ]);
        }
    }

    //login
    public function login(Request $request){
        $this->validate($request, [
            'email'=>'required | string | email | max:255',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/'
        ]);

        $email = $request->input('email');
        $password = $request->input('password');

        //buatmencocokkan data
        $user = User::where('email', $email)->first();//ambil data User berdasarkan email

        if(Auth::attempt([$email, $password])){
            if($user->status == 1){
                if(Hash::check($password, $user->password)){ //password disandingkan apakah match
                    return response()->json([
                        'pesan'=>'Login Berhasil',
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
            'User'=>'required | alpha_dash | max:255',
            'email'=>'required | string | email | max:255',
            'password'=>'required | min:8 | regex:/[a-z]/ | regex:/[A-Z]/ | regex:/[0-9]/ | regex:/[@$!%*#?&]/'
        ]);

        $email = $request->input('email');
        $password = Hash::make($request->input('password'));
        $User = $request->input('User');

        $user = User::where('email', $email)->where('User', $User);

        $data = [
            'password'=>$password,
        ];

        $run = User::where('email', $email)->where('User', $User)->update($data);

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



    //update gambar
    public function updateGambar(Request $request, $id){
        $this->validate($request, [
            'gambar'=>'required'
        ]);

        // $email = User::where('id', $id)->first();
        $email = str_replace(',','.',$id);
        $gambar = $request->file('gambar')->getClientOriginalName();
        $request->file('gambar')->move('upload/'. $gambar);

        $data = [
            //acces pubic path image
            'gambar'=>url('upload/'.$gambar)
        ];

        // return response()->json($gambar);
        // return response()->json($data);

        // $run = User::where('email', $email)->update($data);
        $run = User::where('id',$id)->update($data);

        if($run){
            return response()->json([
                'gambar'=>url('upload/'.$gambar),
                'pesan'=>'Data telah disimpan'
            ]);
        }
    }
}
