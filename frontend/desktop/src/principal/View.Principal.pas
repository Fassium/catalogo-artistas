unit View.Principal;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ExtCtrls, Vcl.StdCtrls, Vcl.Buttons, Vcl.WinXCtrls;

type
  TFrmPrincipal = class(TForm)
    pnlTopo: TPanel;
    lblMenu: TLabel;
    lblTitulo: TLabel;
    lblUsuario: TLabel;
    lblIconeUsuario: TLabel;
    svMenu: TSplitView;
    btnInicio: TSpeedButton;
    btnArtistas: TSpeedButton;
    btnAlbuns: TSpeedButton;
    btnSair: TSpeedButton;
    pnlConteudo: TPanel;
    pnlCartao: TPanel;
    lblBemVindo: TLabel;
    procedure lblMenuClick(Sender: TObject);
    procedure btnSairClick(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  FrmPrincipal: TFrmPrincipal;

implementation

{$R *.dfm}

procedure TFrmPrincipal.lblMenuClick(Sender: TObject);
begin
  svMenu.Opened := not svMenu.Opened;
end;

procedure TFrmPrincipal.btnSairClick(Sender: TObject);
begin
  if MessageDlg('Deseja sair do sistema?', mtConfirmation, [mbYes, mbNo], 0) = mrYes then
    Close;
end;

end.
