program Catalogo;

uses
  Vcl.Forms,
  View.Principal in 'src\principal\View.Principal.pas' {FrmPrincipal},
  Vcl.Themes,
  Vcl.Styles,
  View.Login in 'src\autenticacao\View.Login.pas' {FrmLogin};

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  TStyleManager.TrySetStyle('Windows10');
  Application.CreateForm(TFrmPrincipal, FrmPrincipal);
  Application.CreateForm(TFrmLogin, FrmLogin);
  Application.Run;
end.
