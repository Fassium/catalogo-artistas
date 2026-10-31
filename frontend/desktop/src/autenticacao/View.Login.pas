unit View.Login;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.StdCtrls;

type
  TFrmLogin = class(TForm)
    lblTitulo: TLabel;
    lblSubtitulo: TLabel;
    lblEmail: TLabel;
    lblSenha: TLabel;
    lblMensagem: TLabel;
    edtEmail: TEdit;
    edtSenha: TEdit;
    chkMostrarSenha: TCheckBox;
    btnEntrar: TButton;
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmLogin: TFrmLogin;

implementation

{$R *.dfm}

end.
