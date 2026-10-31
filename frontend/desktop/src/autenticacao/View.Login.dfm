object FrmLogin: TFrmLogin
  Left = 0
  Top = 0
  BorderStyle = bsDialog
  Caption = 'Entrar'
  ClientHeight = 400
  ClientWidth = 380
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -12
  Font.Name = 'Segoe UI'
  Font.Style = []
  Position = poScreenCenter
  TextHeight = 15
  object lblTitulo: TLabel
    Left = 40
    Top = 40
    Width = 245
    Height = 36
    Caption = 'Cat'#225'logo de Artistas'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -27
    Font.Name = 'Segoe UI'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object lblSubtitulo: TLabel
    Left = 40
    Top = 80
    Width = 118
    Height = 15
    Caption = 'Entre para continuar'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clGrayText
    Font.Height = -12
    Font.Name = 'Segoe UI'
    Font.Style = []
    ParentFont = False
  end
  object lblEmail: TLabel
    Left = 40
    Top = 125
    Width = 36
    Height = 15
    Caption = 'E-mail'
  end
  object lblSenha: TLabel
    Left = 40
    Top = 195
    Width = 32
    Height = 15
    Caption = 'Senha'
  end
  object lblMensagem: TLabel
    Left = 40
    Top = 345
    Width = 300
    Height = 17
    Alignment = taCenter
    AutoSize = False
    Caption = 'E-mail ou senha inv'#225'lidos'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clRed
    Font.Height = -12
    Font.Name = 'Segoe UI'
    Font.Style = []
    ParentFont = False
  end
  object edtEmail: TEdit
    Left = 40
    Top = 145
    Width = 300
    Height = 29
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -15
    Font.Name = 'Segoe UI'
    Font.Style = []
    ParentFont = False
    TabOrder = 0
    TextHint = 'seu@email.com'
  end
  object edtSenha: TEdit
    Left = 40
    Top = 215
    Width = 300
    Height = 29
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -15
    Font.Name = 'Segoe UI'
    Font.Style = []
    ParentFont = False
    PasswordChar = '*'
    TabOrder = 1
    TextHint = 'Sua senha'
  end
  object chkMostrarSenha: TCheckBox
    Left = 40
    Top = 258
    Width = 150
    Height = 17
    Caption = 'Mostrar senha'
    TabOrder = 2
  end
  object btnEntrar: TButton
    Left = 40
    Top = 290
    Width = 300
    Height = 42
    Caption = 'Entrar'
    Default = True
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -15
    Font.Name = 'Segoe UI'
    Font.Style = []
    ParentFont = False
    TabOrder = 3
  end
end
